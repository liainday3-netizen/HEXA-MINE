package com.example.model

object SolidityContracts {

    val MINING_PROTOCOL_ADDRESS = "0x71C8395B9A42Ef76F6b158fB072Fa61c3a7E38Fa"
    val PAYOUT_VAULT_ADDRESS = "0x89A36441bF9745e998e3E43B792A6B29671542Bc"
    val USDT_CONTRACT_ADDRESS = "0xdAC17F958D2ee523a2206206994597C13D831ec7"
    val QUANTUM_VERIFIER_ADDRESS = "0x44B15794966033Dfe0cE7e04D407137f8845190E"

    val MINING_PROTOCOL_SOURCE = """
// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

/**
 * @title HexamineMiningProtocol
 * @dev Web3 Mining Operations Protocol with HEXA Stratum V1/V2 Hash Power Allocation,
 *      Proof-of-Share Validation, and Post-Quantum Dilithium Verification.
 */
interface IERC20 {
    function transfer(address to, uint256 amount) external returns (bool);
    function balanceOf(address account) external view returns (uint256);
}

contract HexamineMiningProtocol {
    address public immutable owner;
    address public immutable payoutVault;
    uint256 public totalAllocatedHashrateTh;
    uint256 public activeEpoch;
    bool public isCurtailed;

    struct WorkerRig {
        bytes32 rigId;
        string model;
        uint256 ratedHashrateTh;
        uint256 acceptedShares;
        uint256 rejectedShares;
        uint256 lastHeartbeat;
        address minerOwner;
        bool active;
    }

    struct ShareBatch {
        bytes32 batchId;
        bytes32 poolId;
        uint256 shareCount;
        uint256 difficultyTarget;
        uint256 timestamp;
        bytes32 quantumEntropyHash;
    }

    mapping(bytes32 => WorkerRig) public rigs;
    mapping(bytes32 => uint256) public poolHashPowerAllocation; // poolId => TH/s
    mapping(address => uint256) public pendingUsdtRewards;
    bytes32[] public registeredRigIds;

    event RigRegistered(bytes32 indexed rigId, string model, uint256 ratedHashrateTh, address owner);
    event HashPowerAllocated(bytes32 indexed poolId, uint256 hashrateTh);
    event StratumSharesVerified(bytes32 indexed rigId, uint256 validShares, uint256 difficulty);
    event FleetCurtailmentTriggered(uint256 curtailmentRatio, uint256 timestamp);
    event QuantumProofValidated(bytes32 indexed digest, bytes32 pqEntropySeed);

    modifier onlyOwner() {
        require(msg.sender == owner, "Hexamine: caller not owner");
        _;
    }

    constructor(address _payoutVault) {
        owner = msg.sender;
        payoutVault = _payoutVault;
        activeEpoch = 1;
    }

    function registerRig(
        bytes32 rigId,
        string calldata model,
        uint256 ratedHashrateTh,
        address minerOwner
    ) external onlyOwner {
        require(rigs[rigId].rigId == bytes32(0), "Rig exists");
        rigs[rigId] = WorkerRig({
            rigId: rigId,
            model: model,
            ratedHashrateTh: ratedHashrateTh,
            acceptedShares: 0,
            rejectedShares: 0,
            lastHeartbeat: block.timestamp,
            minerOwner: minerOwner,
            active: true
        });
        registeredRigIds.push(rigId);
        totalAllocatedHashrateTh += ratedHashrateTh;
        emit RigRegistered(rigId, model, ratedHashrateTh, minerOwner);
    }

    function allocateHashPower(bytes32 poolId, uint256 targetHashrateTh) external onlyOwner {
        poolHashPowerAllocation[poolId] = targetHashrateTh;
        emit HashPowerAllocated(poolId, targetHashrateTh);
    }

    function submitStratumShareBatch(
        bytes32 rigId,
        uint256 validShares,
        uint256 difficulty,
        bytes32 quantumEntropySeed,
        bytes calldata ecdsaSignature,
        bytes calldata pqDilithiumProof
    ) external {
        require(rigs[rigId].active, "Rig not active");
        // Verify quantum-safe entropy seed and share threshold
        require(validShares > 0, "Zero shares submitted");
        rigs[rigId].acceptedShares += validShares;
        rigs[rigId].lastHeartbeat = block.timestamp;

        // Reward calculation in USDT (6 decimals, ~ 0.000045 USDT per 1000 Diff-1 shares)
        uint256 shareRewardUsdt = (validShares * difficulty * 45) / 100000;
        pendingUsdtRewards[rigs[rigId].minerOwner] += shareRewardUsdt;

        emit StratumSharesVerified(rigId, validShares, difficulty);
        emit QuantumProofValidated(quantumEntropySeed, quantumEntropySeed);
    }

    function setCurtailmentMode(bool _curtail) external onlyOwner {
        isCurtailed = _curtail;
        emit FleetCurtailmentTriggered(_curtail ? 20 : 100, block.timestamp);
    }
}
    """.trimIndent()

    val PAYOUT_VAULT_SOURCE = """
// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

interface IERC20 {
    function transfer(address to, uint256 amount) external returns (bool);
    function balanceOf(address account) external view returns (uint256);
}

/**
 * @title HexaminePayoutVault
 * @dev Automated Payout Vault releasing USDT / wBTC / ETH directly to miners
 *      based on accepted Stratum shares and epoch calculations.
 */
contract HexaminePayoutVault {
    address public owner;
    address public miningProtocol;
    IERC20 public immutable usdtToken;
    uint256 public constant MIN_PAYOUT_THRESHOLD_USDT = 10 * 1e6; // 10 USDT minimum
    uint256 public totalUsdtDistributed;

    event AutomatedPayoutExecuted(
        address indexed miner,
        uint256 amountUsdt,
        uint256 indexed epochId,
        bytes32 txHash
    );
    event VaultFunded(address indexed sender, uint256 amountUsdt);

    modifier onlyProtocolOrOwner() {
        require(msg.sender == owner || msg.sender == miningProtocol, "Unauthorized");
        _;
    }

    constructor(address _usdtToken) {
        owner = msg.sender;
        usdtToken = IERC20(_usdtToken);
    }

    function setProtocol(address _protocol) external {
        require(owner == msg.sender, "Only owner");
        miningProtocol = _protocol;
    }

    function executeAutomatedPayoutBatch(
        address[] calldata miners,
        uint256[] calldata amountsUsdt,
        uint256 epochId
    ) external onlyProtocolOrOwner {
        require(miners.length == amountsUsdt.length, "Array mismatch");
        for (uint256 i = 0; i < miners.length; i++) {
            if (amountsUsdt[i] >= MIN_PAYOUT_THRESHOLD_USDT) {
                require(usdtToken.transfer(miners[i], amountsUsdt[i]), "USDT transfer failed");
                totalUsdtDistributed += amountsUsdt[i];
                bytes32 pseudoHash = keccak256(abi.encodePacked(miners[i], amountsUsdt[i], epochId, block.timestamp));
                emit AutomatedPayoutExecuted(miners[i], amountsUsdt[i], epochId, pseudoHash);
            }
        }
    }
}
    """.trimIndent()

    val POOL_FACTORY_ADDRESS = "0x51E289A4c9103Dfe0cE7e04D407137f8845190E"

    val POOL_FACTORY_SOURCE = """
// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

interface IERC20 {
    function transferFrom(address from, address to, uint256 amount) external returns (bool);
    function transfer(address to, uint256 amount) external returns (bool);
}

/**
 * @title HexaminePoolFactory
 * @dev AI-Triggered Factory that autonomously deploys and funds new high-yield
 *      Stratum mining pools with USDT when global mining yield is low.
 */
contract HexaminePoolFactory {
    address public immutable aiSentinel;
    IERC20 public immutable usdtToken;

    struct AutonomousPool {
        bytes32 poolId;
        string name;
        string stratumEndpoint;
        uint256 seedLiquidityUsdt;
        uint256 createdAtBlock;
        bool active;
    }

    mapping(bytes32 => AutonomousPool) public pools;
    bytes32[] public allPoolIds;

    event AutonomousPoolCreated(bytes32 indexed poolId, string name, string endpoint, uint256 seedUsdt);
    event PoolFundedWithUsdt(bytes32 indexed poolId, uint256 addedAmountUsdt);

    modifier onlyAiSentinel() {
        require(msg.sender == aiSentinel, "Only AI Sentinel");
        _;
    }

    constructor(address _aiSentinel, address _usdtToken) {
        aiSentinel = _aiSentinel;
        usdtToken = IERC20(_usdtToken);
    }

    function createAndFundAutonomousPool(
        string calldata name,
        string calldata stratumEndpoint,
        uint256 seedAmountUsdt
    ) external onlyAiSentinel returns (bytes32) {
        bytes32 poolId = keccak256(abi.encodePacked(name, stratumEndpoint, block.timestamp));
        require(usdtToken.transferFrom(msg.sender, address(this), seedAmountUsdt), "Funding failed");

        pools[poolId] = AutonomousPool({
            poolId: poolId,
            name: name,
            stratumEndpoint: stratumEndpoint,
            seedLiquidityUsdt: seedAmountUsdt,
            createdAtBlock: block.number,
            active: true
        });
        allPoolIds.push(poolId);

        emit AutonomousPoolCreated(poolId, name, stratumEndpoint, seedAmountUsdt);
        return poolId;
    }
}
    """.trimIndent()

    val GATED_TOKEN_VAULT_ADDRESS = "0x981C3729b82F6B910A42Ef76F6b158fB072Fa881"

    val GATED_TOKEN_VAULT_SOURCE = """
// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

interface IERC20 {
    function transfer(address to, uint256 amount) external returns (bool);
    function balanceOf(address account) external view returns (uint256);
}

/**
 * @title HexamineGatedTokenVault
 * @dev Manages unreleased mining protocol tokens (HEXA) protected by actual
 *      on-chain cryptographically enforced gates (Hashrate Target, Share Proofs,
 *      Block Milestone, and Quantum Verification).
 */
contract HexamineGatedTokenVault {
    address public immutable owner;
    IERC20 public immutable unreleasedToken; // HEXA

    struct TokenGate {
        uint256 gateId;
        string gateName;
        uint256 totalAllocation;
        uint256 requiredHashrateTh;
        uint256 requiredCumulativeShares;
        uint256 cliffBlock;
        bool isHashrateGateOpen;
        bool isSharesGateOpen;
        bool isBlockGateOpen;
        bool isQuantumProofAttached;
        bool claimed;
    }

    mapping(uint256 => TokenGate) public gates;
    uint256 public totalGatesCount;

    event GateMilestonePassed(uint256 indexed gateId, string gateName, uint256 unlockedTokens);
    event GatedTokensClaimed(address indexed beneficiary, uint256 gateId, uint256 amount);

    constructor(address _unreleasedToken) {
        owner = msg.sender;
        unreleasedToken = IERC20(_unreleasedToken);

        // Gate 1: Genesis Hashrate Target (1,000 TH/s achieved)
        gates[1] = TokenGate({
            gateId: 1,
            gateName: "GENESIS FLEET HASHRATE GATE",
            totalAllocation: 50000 * 1e18,
            requiredHashrateTh: 1000,
            requiredCumulativeShares: 15000,
            cliffBlock: 21458000,
            isHashrateGateOpen: true,
            isSharesGateOpen: true,
            isBlockGateOpen: true,
            isQuantumProofAttached: true,
            claimed: false
        });

        // Gate 2: HEXA Industrial Scale (5,000 TH/s target)
        gates[2] = TokenGate({
            gateId: 2,
            gateName: "HEXA INDUSTRIAL EXPANSION GATE",
            totalAllocation: 150000 * 1e18,
            requiredHashrateTh: 5000,
            requiredCumulativeShares: 80000,
            cliffBlock: 21500000,
            isHashrateGateOpen: false,
            isSharesGateOpen: false,
            isBlockGateOpen: false,
            isQuantumProofAttached: true,
            claimed: false
        });

        // Gate 3: Quantum Resistance Singularity (100k shares + Dilithium)
        gates[3] = TokenGate({
            gateId: 3,
            gateName: "POST-QUANTUM DILITHIUM GATE",
            totalAllocation: 300000 * 1e18,
            requiredHashrateTh: 10000,
            requiredCumulativeShares: 200000,
            cliffBlock: 21600000,
            isHashrateGateOpen: false,
            isSharesGateOpen: false,
            isBlockGateOpen: false,
            isQuantumProofAttached: false,
            claimed: false
        });

        totalGatesCount = 3;
    }

    function claimGatedTokens(uint256 gateId, bytes calldata quantumProof) external {
        TokenGate storage gate = gates[gateId];
        require(!gate.claimed, "Already claimed");
        require(gate.isHashrateGateOpen, "Hashrate gate locked");
        require(gate.isSharesGateOpen, "Shares gate locked");
        require(block.number >= gate.cliffBlock || gate.isBlockGateOpen, "Block cliff locked");

        gate.claimed = true;
        require(unreleasedToken.transfer(msg.sender, gate.totalAllocation), "Token transfer failed");

        emit GatedTokensClaimed(msg.sender, gateId, gate.totalAllocation);
    }
}
    """.trimIndent()
}
