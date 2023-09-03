// SPDX-License-Identifier: UNLICENSED

pragma solidity ^0.8.4;

contract BroadcastContract {

    struct Content {
        address producer;
        uint price;
        string contractFileLink;
        string contractHash;
        bool available;
    }

    struct Broadcast {
        address broadcastOwner;
        uint contentId;
        BroadcastStatus status;
        uint viewers;
        string videoId;
        uint startDate;
        uint endDate;
    }

    enum BroadcastStatus {
        PENDING,
        APPROVED,
        REJECTED,
        TERMINATED
    }

    address public oracle;
    address private owner;
    IYoutubeOracle public youtubeOracle;

    constructor(address _oracle) {
        owner = msg.sender;
        oracle = _oracle;
        youtubeOracle = IYoutubeOracle(_oracle);
    }

    Content[] public contents;

    // Mapping producer address to Content struct
    mapping(address => uint[]) public contentsByProducer;

    // Mapping contentId to Broadcast struct
    mapping(uint => Broadcast) public broadcasts;

    mapping(uint => uint[]) public broadcastsByContentId;

    mapping(address => uint[]) public broadcastsByOwner;

    uint public broadcastsCount = 0;

    // Mapping to check if a producer is already linked to a service provider
    mapping(address => address) public broadcastOwnerServiceProviderMapping;

    // Registering new content by producer
    function registerContent(uint _price, string memory _contractLink, string memory _contractHash) public returns (uint) {
        require(_price > 0, "Price must be greater than 0");
        require(bytes(_contractLink).length > 0, "Contract link cannot be empty");
        require(bytes(_contractHash).length > 0, "Contract hash cannot be empty");
        Content memory newContent = Content({
            producer: msg.sender,
            price: _price,
            contractFileLink: _contractLink,
            contractHash: _contractHash,
            available: true
        });


        contents.push(newContent);
        uint contentId = contents.length - 1;
        contentsByProducer[msg.sender].push(contentId);
        return contentId;
    }

    // Linking a producer to a service provider
    function linkBroadcastOwnerToServiceProvider(address _serviceProvider) public {
        broadcastOwnerServiceProviderMapping[msg.sender] = _serviceProvider;
    }

    // Purchasing content by Broadcast Owner
    function purchaseContent(uint _contentId, uint _startDate, uint _endDate, string memory _videoId) public returns (uint) {
        require(broadcastOwnerServiceProviderMapping[msg.sender] != address(0), "BroadcastOwner is not linked to any Service Provider");

        Broadcast memory newBroadcast = Broadcast({
            broadcastOwner: msg.sender,
            contentId: _contentId,
            status: BroadcastStatus.PENDING,
            viewers: 0,
            videoId: _videoId,
            startDate: _startDate,
            endDate: _endDate
        });
        broadcastsCount++;
        broadcasts[broadcastsCount] = newBroadcast;
        broadcastsByContentId[_contentId].push(broadcastsCount);
        broadcastsByOwner[msg.sender].push(broadcastsCount);
        return broadcastsCount;
    }

    function approveBroadcast(uint _broadcastId) public {
        require(msg.sender == contents[broadcasts[_broadcastId].contentId].producer, "Only producer of the content can approve broadcast");
        broadcasts[_broadcastId].status = BroadcastStatus.APPROVED;
    }

    function rejectBroadcast(uint _broadcastId) public {
        require(msg.sender == contents[broadcasts[_broadcastId].contentId].producer, "Only producer of the content can reject broadcast");
        broadcasts[_broadcastId].status = BroadcastStatus.REJECTED;
    }

    // Oracle updates viewer count
    function updateViewerCount(uint _broadcastId, uint _viewers) public {
        require(msg.sender == oracle, "Only oracle can update viewer count");
        broadcasts[_broadcastId].viewers = _viewers;
    }

    function setUnavailableContent(uint _contentId) public {
        require(msg.sender == contents[_contentId].producer, "Only producer of the content can set content unavailable");
        //check if there is broadcast that are not not terminated
        for (uint i = 0; i < broadcastsCount; i++) {
            if (broadcasts[i].contentId == _contentId && broadcasts[i].endDate > block.timestamp) {
                revert("Cannot delete content with active broadcasts");
            }
        }
        contents[_contentId].available = false;
    }

    function setOracle(address _oracle) public {
        //check if the sender is the oracle or the user that deployed the contract
        require(msg.sender == oracle, "Only oracle can set the new oracle address");
        oracle = _oracle;
    }

    function getContents() public view returns (Content[] memory) {
        return contents;
    }

    function getContent(uint _contentId) public view returns (Content memory) {
        return contents[_contentId];
    }

    function getBroadcastsByContentId(uint _contentId, address user) public view returns (Broadcast[] memory) {
        require(msg.sender == owner || msg.sender == user, "Only owner or user can get broadcasts by content id");
        require(contents[_contentId].producer == user, "Only producer of the content can get broadcasts by content id");
        require(_contentId <= contents.length, "Content does not exist");
        uint[] memory ids = broadcastsByContentId[_contentId];
        Broadcast[] memory result = new Broadcast[](ids.length);

        for (uint i = 0; i < ids.length; i++) {
            result[i] = broadcasts[ids[i]];
        }

        return result;
    }

    function getMyBroadcasts(address user) public view returns (Broadcast[] memory) {
        require(msg.sender == owner || msg.sender == user, "Only owner or user can get contents by producer");
        uint[] memory ids = broadcastsByOwner[user];
        Broadcast[] memory result = new Broadcast[](ids.length);

        for (uint i = 0; i < ids.length; i++) {
            result[i] = broadcasts[ids[i]];
        }

        return result;
    }

    function getMyContents(address user) public view returns (Content[] memory) {
        require(msg.sender == owner || msg.sender == user, "Only owner or user can get contents by producer");
        uint[] memory ids = contentsByProducer[user];
        Content[] memory result = new Content[](ids.length);

        for (uint i = 0; i < ids.length; i++) {
            result[i] = contents[ids[i]];
        }

        return result;
    }

    function getVideoIdFromBroadcast(uint _broadcastId) public view returns (string memory) {
        require(_broadcastId <= broadcastsCount, "Broadcast does not exist") ;
        require(oracle == msg.sender, "Only oracle can get broadcast by id");
        return broadcasts[_broadcastId].videoId;
    }

    function checkAndTerminateAllBroadcasts() public {
        require(msg.sender == owner, "Only owner can terminate broadcasts");
        for (uint i = 1; i <= broadcastsCount; i++) {
            Broadcast storage broadcast = broadcasts[i];

            if (broadcast.status == BroadcastStatus.APPROVED && broadcast.endDate < block.timestamp) {
                youtubeOracle.fetchVideoViews(i);

                broadcast.status = BroadcastStatus.TERMINATED;
            }
        }
    }
}

interface IYoutubeOracle {
    function fetchVideoViews(uint _broadcastId) external;
}