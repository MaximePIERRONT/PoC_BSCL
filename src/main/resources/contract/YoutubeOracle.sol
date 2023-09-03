// SPDX-License-Identifier: UNLICENSED

pragma solidity ^0.8.4;

import "./contract/BroadcastContract.sol";

contract YoutubeOracle {

    address private owner;
    IBroadcastContract public broadcastContract;
    event NewYoutubeRequest(uint _broadcastId);
    address private broadcastContractAddress = address (0x0);

    constructor() {
        owner = msg.sender;
        broadcastContract = IBroadcastContract(address (0x0));
    }

    function fetchVideoViews(uint _broadcastId) public {
        require(msg.sender == broadcastContractAddress, "Only the broadcastContract can make a request");
        emit NewYoutubeRequest(_broadcastId);
    }

    function setVideoViews(uint _broadcastId, uint256 _views) public {
        require(msg.sender == owner, "Only the owner can set the views");

        // Updating viewer count in BroadcastContract
        broadcastContract.updateViewerCount(_broadcastId, _views);
    }

    function setBroadcastContractAddress(address _broadcastContractAddress) public {
        require(msg.sender == owner, "Only the owner can set the broadcast contract address");
        broadcastContractAddress = _broadcastContractAddress;
        broadcastContract = IBroadcastContract(_broadcastContractAddress);
    }

    function getBroadcastById(uint _broadcastId) public view returns (string memory) {
        return broadcastContract.getVideoIdFromBroadcast(_broadcastId);
    }
}

// Interface of the BroadcastContract
interface IBroadcastContract {
    function updateViewerCount(uint broadcastId, uint viewers) external;

    function getVideoIdFromBroadcast(uint _broadcastId) external view returns (string memory);
}
