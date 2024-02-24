# Facebook Clone

![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![ElasticSearch](https://img.shields.io/badge/-ElasticSearch-005571?style=for-the-badge&logo=elasticsearch)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)

This is a full-stack Facebook clone built with React and Spring Boot microservices

https://github.com/Yash-Handa/facebook-clone/assets/32840183/69f01dc0-075c-4573-857c-c9abcd7cbc87

## Features 😎

- Offline-first PWA responsive web app built with React.
- **User Registration**: Users can create an account with a unique username and password.
- **Profiles**: Users can create profiles with avatar photos, cover photos, and personal information.
- **Post Updates**: Users can post updates - text and photos, on their profiles and view updates from others.
- **News Feed**: Users can see updates from their friends on their news feed.
- **Friend Request**: Users can send (withdraw) friend requests to connect with others and accept or decline friend requests from others.
- **Notifications**: Users can receive Real-time notifications (through WebSockets) for friend requests, messages, and updates from others.
- **Search**: Users can search for other users and post across Facebook.

## Technologies Used ⚒️

#### FrontEnd

  - Front end is built with [React](https://react.dev/) (v18.2.0).
  - Styles are written in [SCSS](https://sass-lang.com/).
  - Responsive layouts for Desktop, Tab, and Mobile views.
  - Offline first PWA app
  - Across the app Light and Dark themes

#### APIs

  - REST APIs communicate with micro-services through Gateway
  - Real-time notifications are sent through Web-Sockets ([STOMP protocol](https://stomp.github.io/))

#### Security
  - [JWT authentication](https://jwt.io/) is required to communicate with the backend.
  - REST API calls must have a valid JWT token attached to the `Authentication` header (except login and register).
  - Web-Socket connections must be established with a valid JWT token attached to the `Authentication` request param.
  - All micro-services can be accessed only through the Gateway.

#### BackEnd
  - Back end is built with [Spring Boot](https://spring.io/projects/spring-boot) (v3.1.1) and [JDK](https://www.oracle.com/java) (v17).
  - [Apache Maven](https://maven.apache.org/) for dependency management.

#### Gateway/ Consul
  - [Consul](https://www.consul.io/) is used for service registry and discovery by the Gateway.
  - Load-balancing and inter-micro-service communication is also taken care of by the consul.
  - It also provides a Key/Value store for micro-services.

#### Database
  - [MongoDB](https://www.mongodb.com/) is used in all micro-services and connected through [MongoTemplate](https://docs.spring.io/spring-data/mongodb/docs/current/api/org/springframework/data/mongodb/core/MongoTemplate.html) (Spring Data MongoDB v4.1.1).
  - [GridFS](https://www.mongodb.com/docs/manual/core/gridfs/) (MongoDB file system) is used for storing avatar pic, post images, etc.
  - [ElasticSearch](https://www.elastic.co/) is used alongside MongoDB for providing blazing-fast user and post searches.

#### Catching
  - [Guava](https://github.com/google/guava) Caching is used for faster users and post fetch.
  - It is also used for user feed creation.

#### Message Brocker
  - [Apache Kafka](https://kafka.apache.org/) is used for event-driven communication between micro-services.
  - Different micro-services publish messages to three topics: `userTopic`, `postTopic`, and `friendTopic`.
  - All messages are consumed by the `Facebook-NotificationMS` micro-service.

## Architecture 🏗️

![Architecture](/.github/assets/FaceBook_Architecture.png?raw=true "Facebook Architecture Diagram")

The architecture of the application has been mainly divided into 5 micro-services:

#### Facebook_Gateway

The `Facebook_Gateway` is responsible for connecting the rest of the world with the backend application. It also secures public-facing APIs with JWT token verification (except login and register). Any request coming to `Facebook_Gateway` must have an `Authentication` header (for REST APIs) or `Authentication` request param (for WebSocket connection). Here the Authentication token is checked for correctness and expiration. If the token is valid then the `userId` is extracted and put in the header of the incoming request and the request is forwarded to the corresponding micro-service.

#### Facebook_UserMS

The `Facebook_UserMS` comprises of:

- One Spring Boot application (`lb://Facebook-UserMS`).
- Three MongoDB collections
  - `user` - for saving all user-related information.
  - `fs.files`, `fs.chunks` - for implementing GridFS store to save user avatar/ cover.
- One Guava Catch store.

The `Facebook_UserMS` is responsible for all user-related endpoints and data:

- User registration and sign-in with JWT token creation.
- Edit User profiles, uploading and retrieving avatar pics.
- Get user info by `userId`, get currently logged-in user info.
- Get the list of friends of a user.
- Save user data to ElasticSearch and retrieve from there as well.
- Publish messages to `userTopic` of Kafka.

#### Facebook_PostMS

The `Facebook_PostMS` comprises of:

- One Spring Boot application (`lb://Facebook-PostMS`).
- Three MongoDB collections
  - `post` - for saving all post data.
  - `fs.files`, `fs.chunks` - for implementing GridFS store to save post pictures.
- One Guava Catch store.

The `Facebook_PostMS` is responsible for all post-related endpoints and data:

- Creation of posts, get posts of an individual (with pagination).
- Uploading and retrieving of post images.
- Creation and maintenance of feeds of a user using Guava catching.
- Save post-related data to ElasticSearch and retrieve from there as well.
- Publish messages (Post Creation) to `postTopic` of Kafka.

#### Facebook_FriendMS

The `Facebook_FriendMS` comprises of:

- One Spring Boot application (`lb://Facebook-FriendMS`).
- One MongoDB collection
  - `friendRequest` - for saving all friend request information (From, To, Status, CreatedAt).

The `Facebook_FriendMS` is responsible for all friend request-related endpoints and data:

- 

#### Facebook_NotificationMS

## Screen Shorts 📸

| Registeration Page                                    | SignIn Page                                           |
| ----------------------------------------------------- | --------------------------------------------- |
| ![Registeration Page](/.github/assets/Register_Page.png) | ![SignIn Page](/.github/assets/SignIn_Page.png) |

| Desktop UI                                    | Desktop UI Dark                                           |
| ----------------------------------------------------- | --------------------------------------------- |
| ![Desktop UI](/.github/assets/Desktop_UI.png) | ![Desktop UI Dark](/.github/assets/Desktop_UI_Dark.png) |

| Notifications                                    |  Seach                              |
| ----------------------------------------------------- | --------------------------------------------- |
| ![Notifications](/.github/assets/Notifications.png) | ![Seach](/.github/assets/Seach.png) |

| Mobile UI                                    | Mobile UI Dark                                           |
| ----------------------------------------------------- | --------------------------------------------- |
| ![Desktop UI](/.github/assets/Mobile_UI.png) | ![Desktop UI Dark](/.github/assets/Mobile_UI_Dark.png) |

| Edit Profile Page                                    | Tab UI                                |
| ----------------------------------------------------- | --------------------------------------------- |
| ![Edit Profile Page](/.github/assets/Edit_Profile_Page.png) | ![Tab UI](/.github/assets/Tab_UI.png) |
