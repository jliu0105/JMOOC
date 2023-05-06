# JMOOC: An online Learning Platform
JMOOC is an online learning platform that offers a wide range of courses for users to explore, review, and purchase. The platform comes with an interactive control panel for maintainers to manage course content and user information. It leverages MyBatis to provide a caching layer for queries and transactions, resulting in improved performance.
<br><br>
Built with Java, Spring Boot, Spring Cloud, Vue, JavaScript, Redis, MySQL, Bootstrap, Maven, and Docker.

## Table of Contents
[Features](#features) <br>
[Requirement](#requirement)<br>
[Installation](#requirement)<br>
[Usage](#usage)<br>
[Contribution](#contribution)<br>


## Features 
* __User Authentication__: Users can create accounts and log in to access the platform securely.
* __Course Catalog__: A comprehensive list of courses, sortable by category, rating, and other filters.
* __Course Reviews__: Users can review and rate courses, providing valuable feedback for potential learners and course creators.
* __Purchase Courses__: Secure payment system allowing users to purchase their desired courses.
* __Interactive Control Panel__: Maintain the platform, edit course details, upload files, videos, and review user information.
* __MyBatis Caching__: Optimized performance using MyBatis caching for queries and transactions.

## Requirement
* Java 8+
* Maven 3.6+
* Docker
* MySQL
* Redis

## Installation

### clone the repo
```bash
git clone https://github.com/jliu0105/JMOOC.git
```

### Install the required dependencies:
```bash
npm install
```

### Build the project:
```bash
mvn clean package
```
### Build the docker image
```bash
docker build -t jmooc .
```

### Run the docker container:
```bash
docker run -p 8080:8080 --name jmooc jmooc
```
Access the application at http://localhost:8080.

## Usage

### User
* Register for an account or log in to an existing one.
* Browse the course catalog and filter the courses as needed.
* Review and rate courses to provide feedback.
* Purchase courses using the secure payment system.

### Maintainer
* Access the interactive control panel using the maintainer's credentials.
* Edit course details, such as title, description, and category.
* Upload files and videos for the courses.
* Review and manage user information.

## Contribution
We welcome contributions from the community! If you're interested in contributing to this project, please follow these steps:

1. Fork the repository
2. Create a new branch for your changes
3. Commit your changes and push them to your fork
4. Create a pull request for your changes

