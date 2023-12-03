## Project description

This is a User management application implemented using the following stack:

* **Spring boot** - Provides the rest endpoints, authentication and business logic
* **PostgrsSQL** - Stores user data
* **Hazelcast** - Provides caching implementation
* **Docker** - Provides containerization and is used to run all parts of application together.

## How to run

**To run the application it is required to have docker installed.**

project contains script file called `start.sh` or `start.sh` to run the application just run the execute the script.

In docker 4 containers will be started:

* backend application
* PostgreSQL
* hazelcast instance
* hazelcast management ui

## Default users

When application is started 2 default users will already be pre-populated into database

* admin - `username: admin@test.com, password: admin`
* user123 - `username: bla@test.com, password: client`

## API

swagger: http://localhost:8080/swagger-ui/index.html

# User Management API

## Admin User Controller

### Get User by ID

- **Method:** GET
- **Endpoint:** /api//admin/users/{id}
- **Description:** Get user information by ID.

### Update User

- **Method:** PUT
- **Endpoint:** /api/admin/users/{id}
- **Description:** Update user by ID.

### Delete User

- **Method:** DELETE
- **Endpoint:** /api/admin/users/{id}
- **Description:** Delete user by ID.

### Create User

- **Method:** POST
- **Endpoint:** /api/admin/users
- **Description:** Create a new user.

### get Users

- **Method:** GET
- **Endpoint:** /api/admin/users
- **Description:** filter players by activation status.

## User Controller

## Get Authenticated User

- **Method:** GET
- **Endpoint:** /api/users/me
- **Description:** Get authenticated user using bearer token.

## Update Authenticated User

- **Method:** PUT
- **Endpoint:** /api/users/me
- **Description:** Update authenticated user

### Delete User

- **Method:** DELETE
- **Endpoint:** /api/users/me
- **Description:** Delete user by ID.

## Auth Controller

### User Login

- **Method:** POST
- **Endpoint:** /api/auth/login
- **Description:** Authenticate user and return access token. Deactivated(Deleted) users cannot login.

## Accessing Hazelcast Management Center UI

The Hazelcast Management Center UI can be accessed through a web browser

- **URL:** http://localhost:48080