# JobFinder

Full Stack web application built with Spring Boot and Next.js that simulates the job search process. It features full CRUD functionality, allowing users to easily manage companies and job listings, and also provides a seamless search functionality. Job seekers can browse, search for jobs based on various criteria, making the job-hunting process more efficient and user-friendly.

## Technologies
- ### Backend 
    [![Backend](https://skillicons.dev/icons?i=docker,spring,kotlin,postgres)](https://skillicons.dev)

- ### Frontend
    [![Frontend](https://skillicons.dev/icons?i=ts,html,css,tailwind,nodejs,next)](https://skillicons.dev)

## Prerequisites
Before you begin, make sure you have met the following requirements:

- Java Development Kit (JDK) installed
- Node.js and npm installed
- IDE with support for Spring Boot and Next.js development
- Docker Compose installed

## Setup

### Backend
```
$ docker-compose up
```
Then build and run **JobFinderApplication.kt**

### Frontend
```
$ cd frontend
$ npm i
$ npm run build
$ npm run start
```

## Swagger
Full list of available REST endpoints could be found in Swagger UI.

**http://localhost:8080/swagger-ui/index.html**

## Features

### Search jobs
In the main page you can scroll through many kinds of jobs, that are available to apply. There is a search field, if you have anything specific to look for and there are many filters that help narrow down the results.

![](/docs/pictures/search.gif)

### Sort search results
You can sort the results by any information given.
<br>E.g. if you like to see the most recent jobs first, you can sort them by clicking on the Updated column header.

![](/docs/pictures/sort.gif)

### Create company
For admins, there is a page dedicated for editing and creating companies. Simply click on the 'New company' button and give the organization's name, logo and description. The latter has a special Markdown editor to make it more customizeable.

![](/docs/pictures/company_create.gif)

### Read company
Each company has a dedicated site where their informations can be seen. Also if there is any jobs uploaded by this company, you can see them listed on the bottom.

![](/docs/pictures/company_read.gif)

### Update company
If you want to change an already existing company, there is an 'Edit' button, where you can change anything.

![](/docs/pictures/company_update.gif)

### Delete company
If the company doesn't exist anymore or doesn't want to upload any jobs in the future, it can be deleted.

![](/docs/pictures/company_delete.gif)

### Create job
There is a different page dedicated for jobs for admins. Here you can create a new job, the same way you did with the companies. There are more information that has to be filled, including choosing the company the job belongs to. This can only be an already existing company.

![](/docs/pictures/job_create.gif)

### Read job
Each job has it's dedicated site where their informations can be seen, with an 'Apply' button.

![](/docs/pictures/job_read.gif)

### Edit job
You can also edit any information given in the job.

![](/docs/pictures/job_update.gif)

### Delete job
If the job is no longer available, it can be deleted.

![](/docs/pictures/job_delete.gif)

## License
[MIT](https://choosealicense.com/licenses/mit/)

