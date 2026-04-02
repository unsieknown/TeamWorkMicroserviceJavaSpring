## Application Setup

To properly configure and run the application, you need to create an environment file with all required variables.

---

### 1. `.env` File Location

Create a `.env` file in the following directory:
deployment/docker

The file **must be placed in the same directory as the `docker-compose` file**.

---

### 2. Environment Variables

Inside the `.env` file, define the following variables:
- USER_MYSQL_USER=
- USER_MYSQL_PASSWORD=
- USER_MYSQL_ROOT_PASSWORD=
- USER_MYSQL_DATABASE=team-work-user-s

- STORAGE_MYSQL_USER=
- STORAGE_MYSQL_PASSWORD=
- STORAGE_MYSQL_ROOT_PASSWORD=
- STORAGE_MYSQL_DATABASE=team-work-storage-s

- STORAGE_MONGO_DB_NAME=team-work-storage
- STORAGE_MONGO_DB_USER=team-work-storage-admin
- STORAGE_MONGO_DB_PASSWORD=superSecretPassword
- STORAGE_MONGO_ROOT_USERNAME=
- STORAGE_MONGO_ROOT_PASSWORD=

- STORAGE_ROOT=/data/files_storage
- PROFILE_IMAGES_ROOT=/data/profile_images_storage

- NOTES_MONGO_DB_NAME=team-work-notes
- NOTES_MONGO_DB_USER=team-work-notes-admin
- NOTES_MONGO_DB_PASSWORD=superSecretPassword
- NOTES_MONGO_ROOT_USERNAME=
- NOTES_MONGO_ROOT_PASSWORD=

- TEAM_MYSQL_USER=
- TEAM_MYSQL_PASSWORD=
- TEAM_MYSQL_ROOT_PASSWORD=
- TEAM_MYSQL_DATABASE=team-work-team-s

- BOARD_MONGO_DB_NAME=team-work-board
- BOARD_MONGO_DB_USER=team-work-board-admin
- BOARD_MONGO_DB_PASSWORD=superSecretPassword
- BOARD_MONGO_ROOT_USERNAME=
- BOARD_MONGO_ROOT_PASSWORD=

- AUTH_MYSQL_USER=
- AUTH_MYSQL_PASSWORD=
- AUTH_MYSQL_ROOT_PASSWORD=
- AUTH_MYSQL_DATABASE=team-work-auth-s

- MAIL_ADDR=
- MAIL_PASSWD=


---

### 3. MySQL Configuration (IMPORTANT)

All variables related to MySQL:

- `*_MYSQL_USER`
- `*_MYSQL_PASSWORD`
- `*_MYSQL_ROOT_PASSWORD`
- `*_MYSQL_DATABASE`

**Database names (`*_MYSQL_DATABASE`) must NOT be changed.**

These values are used directly in Flyway migration scripts. Changing them will break database initialization.

You can safely configure:

- usernames  
- passwords  

---

### 4. MongoDB Configuration

For MongoDB variables:

- `*_MONGO_DB_NAME`
- `*_MONGO_DB_USER`
- `*_MONGO_DB_PASSWORD`
- `*_MONGO_ROOT_USERNAME`
- `*_MONGO_ROOT_PASSWORD`

You **can modify these values**, but if you do:

You **must also update corresponding files** located in:
./mongodb

These files are responsible for:

- creating users  
- initializing databases  
- setting collections and indexes  

All values must remain consistent between `.env` and these initialization scripts.

---

### 5. Mail Configuration

To enable email functionality:

1. Create an application in your Google account (App Password).
2. Set the following variables:
- MAIL_ADDR=your_email@gmail.com
- MAIL_PASSWD=generated_app_password

Important:

- Use a **Google App Password**, not your normal account password  
- Remove **all whitespace characters** from the generated password before using it  

---

### 6. Summary

- `.env` must be in `deployment/docker` next to `docker-compose`  
- MySQL database names must NOT be changed  
- MongoDB values can be changed, but require updates in `./mongodb`  
- Storage paths must be valid  
- Mail requires a properly configured Google App Password  

---

This completes the required setup to run the application.
