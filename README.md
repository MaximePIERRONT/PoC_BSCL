---
# "Blockchain Sharing Content Livestreaming (BSCL) - Proof of Concept (PoC)"
---

## Introduction

The BSCL project aims to showcase the feasibility and reliability of implementing specific rules for protected live content sharing through the Ethereum blockchain.

## Architecture

### Smart Contracts

- **BroadcastContract**: Manages content. Owners can register and purchase content. The content status can be changed, and an oracle is integrated to fetch the number of viewers.
  
- **YoutubeOracle**: Acts as a bridge between the broadcasting platform and YouTube. It fetches the view count of a video on YouTube and then updates the BroadcastContract.

### Deployment and Interactions

Deployment follows a two-step process:
1. Deploy the Oracle.
2. Deploy the BroadcastContract linking it with the Oracle's address.

### User Interface

To facilitate interactions with the blockchain, a Restful API is established. This interface requires:
- A programming language (Java 17).
- A framework (Transition from Quarkus to Spring).
- A blockchain library (Web3j).
- An authentication mechanism (JWT).

## Technical Choices

1. **Pattern "Delegated Transactions"**: Ensures transaction security by ensuring only legitimate owners initiate transactions.

2. **"Content" Structure**: Optimizes storage by storing only the data's hash and a link to the actual data.

3. **Oracle**: Enables seamless integration of external data into the blockchain.

4. **Authentication via JWT**: Provides secure and efficient authentication.

5. **Programming Language - Java 17**: Robust, reliable, and supported by a broad community.

6. **Framework - Spring**: Offers flexibility and robustness, ideal for enterprise applications.

7. **Blockchain Library - Web3j**: Provides a Java-Ethereum interface to facilitate interactions with the blockchain.

## Validation

Behavior-Driven Development (BDD) is used to ensure the system meets the users' needs and requirements.

## Conclusion

The BSCL PoC seeks to demonstrate the viability and efficiency of blockchain for live content sharing. With a strong architecture and justified technical choices, the PoC is well-positioned to address the current broadcasting industry challenges.
