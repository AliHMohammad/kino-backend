# startcode-with-security

[Supplementing Google Doc](https://docs.google.com/document/d/1k5T7rRSrGetjuMdYoErk4ToZTk22QyBpR-x17nmtRos/edit?usp=sharing)

Clone (prefered) or fork the project
If cloned delete the hidden .git folder and do a new git init

Se this [short video](https://www.youtube.com/watch?v=aISFmtX-vfA)
for how to use the code for the cars'R' Us project and future projects this semester where you need Security

### Info

If you use this as the very start of your own projects, delete the security-zipped folder after cloning.
These files are meant to include this project into an EXISTING project, as described in the supplementing Google Doc

### Short text-version of what the video above goes through
- Clone/fork the project, and do what it takes to create you own git-folder, and after that, git-repo
- Create the MySQL database you plan to use for the project on your local MySQL instance
- Add the environment variables to Intellij, necessary to connect to the database. Remember to uncomment where these are read in **application.properties**, otherwise it will use H2.
- Rename (REFACTOR) the package name rename_me into something that makes sense
- If you only need plain users (username=primary key, email and password) use it as is
- If you need specialised users, extend the `UserWithRoles` class
- Default POST login-endpoint is here: **/api/auth/login**
  
  Add this JSON with the login request:
  ```
  {
  "username" : "user1",
  "password" : "test12"
  }
  ```
