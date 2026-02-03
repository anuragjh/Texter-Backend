
---

# System Overview

This is a **single-server, anonymous real-time chat system** built using **Spring Boot, WebSockets (STOMP), and MongoDB**.

The system uses **REST APIs** for room lifecycle management and **WebSockets** for real-time message delivery. MongoDB acts as the **single source of truth** for rooms, users, messages, and moderation state.

### Key Characteristics

* No user accounts or authentication
* Public and private chat rooms
* Session-based admin authority
* Real-time messaging using WebSockets
* Persistent chat history
* Admin-controlled moderation
* Automatic room cleanup

---

## High-Level Architecture

```
Frontend (Web / Mobile)
        |
        | REST APIs (room lifecycle, moderation, history)
        | WebSocket (real-time chat & system events)
        v
Spring Boot Backend
        |
        v
MongoDB
```

---

## Core Design Principles

### 1. Identity Without Accounts

Since there is no login or authentication system:

* Each browser tab or client connection corresponds to **one WebSocket session**
* `sessionId` (assigned by WebSocket) acts as a **temporary identity**
* `username` is a **display name**, unique per room

This design keeps the system:

* Simple to use
* Anonymous by default
* Suitable for temporary or ad-hoc chat rooms

---

### 2. Data Model and Responsibilities

| Collection      | Purpose                                    |
| --------------- | ------------------------------------------ |
| `rooms`         | Room metadata, privacy flag, admin session |
| `room_members`  | Active users currently inside rooms        |
| `join_requests` | Pending join requests for private rooms    |
| `messages`      | All chat messages and system messages      |

MongoDB is always the authoritative source of state.
WebSockets only transport events; they do not store state.

---

## Public Room Workflow

### Step 1: Create Room

**Frontend**

```
POST /api/v1/rooms
```

**Backend**

* Generates a unique `roomCode`
* Saves room metadata (public/private)
* No admin session is assigned yet

**Response**

```json
{
  "roomCode": "X7KQ2"
}
```

---

### Step 2: Join Room

**Frontend**

```
POST /api/v1/rooms/join
```

```json
{
  "roomCode": "X7KQ2",
  "username": "XYZ"
}
```

**Backend Validation**

* Room exists
* Username is unique within the room
* Room is public → allow immediate join
* First user becomes admin

**State Changes**

* Entry added to `room_members`
* `rooms.adminSessionId` set
* System message created and broadcast:

> “XYZ joined the room”

---

### Step 3: WebSocket Connection

**Frontend**

```js
stompClient.connect(
  { roomCode: "X7KQ2" },
  onConnect
);
```

* `roomCode` is stored in WebSocket headers
* Used later for disconnect handling and cleanup

---

### Step 4: Messaging

**Frontend**

```js
stompClient.send("/app/chat.send", {}, {
  roomCode,
  sender,
  content
});
```

**Backend Processing**

* Rate limit check
* Room existence check
* Membership validation
* Message persistence
* Broadcast to `/topic/room/X7KQ2`

**Result**

* Real-time chat delivery
* Message stored in MongoDB

---

## Private Room Workflow

Private rooms follow the same flow with an **approval step added**.

---

### Step 1: Create Private Room

```json
{
  "roomName": "Private Chat",
  "isPrivate": true,
  "adminUsername": "XYZ"
}
```

Room is saved with `isPrivate = true`.

---

### Step 2: User Requests to Join

**Frontend**

```
POST /api/v1/rooms/join
```

```json
{
  "roomCode": "X7KQ2",
  "username": "Rahul"
}
```

**Backend**

* Detects private room
* Saves request in `join_requests`
* Returns:

```json
{ "status": "PENDING" }
```

**Frontend State**

* Displays: “Waiting for admin approval”

---

### Step 3: Admin Views Join Requests

**Frontend (Admin Panel)**

```
GET /api/v1/rooms/{roomCode}/join-requests
```

**Result**

```
Rahul wants to join
```

---

### Step 4: Admin Decision

#### Approve

```
POST /api/v1/rooms/admin/approve
```

**Backend Actions**

* Verifies admin session
* Moves user to `room_members`
* Deletes join request
* Broadcasts system message:

> “Rahul joined the room”

#### Reject

* Join request is deleted
* User remains outside the room

---

## Admin Capabilities

Admin authority is determined by `adminSessionId`.

Admins can:

* Approve or reject join requests
* Kick users from the room
* Destroy the room at any time

---

### Kick User

```
POST /api/v1/rooms/admin/kick
```

**Backend**

* Removes user from `room_members`
* Broadcasts:

> “Rahul was kicked by admin”

**Frontend**

* Kicked user is disconnected automatically

---

### Admin Leaves the Room

* Detected via WebSocket disconnect
* If admin disconnects:

  * Room is destroyed
  * All members are removed
  * System message broadcast:

> “Room closed by admin”

* Frontend redirects users to home

---

## System Messages

System messages are stored in the same `messages` collection with `type = SYSTEM`.

Examples:

* User joined
* User left
* User kicked
* Room closed

Frontend renders them differently (centered, muted style), keeping chat history consistent and understandable.

---

## Message History

On room load, frontend fetches history:

```
GET /api/v1/rooms/messages/{roomCode}?limit=50
```

Behavior:

* Loads last 50 messages (maximum)
* Then connects to WebSocket for live updates

This is the standard industry pattern used by modern chat applications.

---

## Rate Limiting

Each WebSocket session is limited to:

* 10 messages per 10 seconds

This prevents spam without requiring authentication.

---

## Cleanup and Stability

| Event              | Backend Action            |
| ------------------ | ------------------------- |
| User disconnects   | Removed from room_members |
| Room becomes empty | Room deleted              |
| Admin disconnects  | Room destroyed            |
| Server restarts    | Rooms reset (by design)   |

This ensures:

* No ghost users
* No stale rooms
* No memory leaks

---

## Conclusion
This system design provides a robust, anonymous real-time chat experience with essential features like room management, moderation, and message persistence. The use of Spring Boot, WebSockets, and MongoDB ensures scalability and maintainability while keeping the architecture straightforward and efficient.
