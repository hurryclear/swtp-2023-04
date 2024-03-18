# API Documentation

## Introduction

This README provides detailed information about the API endpoints available for the frontend and backend teams. It is designed to facilitate clear communication and ensure that both teams are aligned on the usage of the API.

## Base URLs

### Local Development

Use the following base URL for local development:

```
http://localhost:3000/api
```

### Virtual Machine (VM) Deployment

For the deployed application on a VM, use the VM's IP address:

```
http://VM_IP_ADDRESS:3000/api
```

**Note:** The base URL is set automatically by the Docker Compose file. Use `docker-compose-prod.yml` for production with the VM's IP address, and `docker-compose.yml` for local development with `localhost:3000`.

Frontend developers should use relative paths in Axios requests:


```javascript
axios.get('/api/auth/logout');
```

## Unsecured Endpoints

### Login

<details>
<summary>Click to expand Login endpoint details</summary>

#### Endpoint
`POST /api/auth/login`

#### Request Body
To log in, send a POST request to the endpoint with the following JSON payload in the request body:

```json
{
  "username": "exampleUser",
  "password": "examplePass"
}
```

#### Response Body

```json
{
  "token":"here.comes.your.token",
  "role": "ROLE_OFFICE"
}
```

</details>

### Logout

<details>
<summary>Click to expand Logout endpoint details</summary>

#### Endpoint
`GET /api/auth/logout`

#### Description
Logs out the user and invalidates the session token. Returns an OK response if the logout on the server side succeeded.

#### Response
`200 OK Response`

</details>

### Submitting Application

<details>
<summary>Click to expand Submitting Application endpoint details</summary>

#### Endpoint
`POST /api/student/submitApplication`

#### Description
Allows a student to submit their application for the first time. The response includes a unique application number and all other submitted details, enabling the student to view the details again and download a summary as a PDF. The frontend should redirect to "View Application" and log in the user automatically upon receiving the response.

#### Request Body
```json
    {
  "meta": {
    "dateOfSubmission": "2023-12-31T22:30:42.103Z",
    "dateLastEdited": "2024-01-14T14:12:14.675Z",
    "status": ""
  },
  "university": {
    "name": "University of Regenbogenland",
    "country": "Regenbogencountry",
    "website": "http://www.uni-halle.de/"
  },
  "courseOfStudy": {
    "old": "B. Sc. Informatik",
    "new": "B.Sc. Informatik"
  },
  "moduleMappings": [
    {
      "meta": {
        "key": 0
      },
      "previousModules": [
        {
          "meta": {
            "key": 1,
            "comments": {
              "student": "War nicht so cool"
            }
          },
          "courseOfStudy": "B. Sc. Informatik",
          "university": {
            "web_pages": [
              "http://www.regenbogen.re/"
            ],
            "name": "University of Regenbogenland",
            "country": "Regenbogenland"
          },
          "id": "69",
          "name": "AlgoDat 0.5",
          "credits": 4
        }
      ],
      "modulesToBeCredited": [
        3,
        4
      ]
    }
  ]
}
```

#### Response Body
```json
{
  "applicationNumber": "uniqueApplicationNumber"
}
```

</details>

### Reviewing Application

<details>
<summary>Click to expand Reviewing Application endpoint details</summary>

#### Endpoint
`GET /api/student/reviewApplication`

#### Description
Student get his application for reviewing.

#### Request Body
```http request
  /reviewApplication?applicationID=123-123-123-123
```

#### Response Body
```json
{
  "applicationData": {
    "applicationID": "23-23-23-23-23-23-23-23-23-23-23",
    "status": "edited approval",
    "formalRejection": "Begründungsfeld für formale Ablehnung des Gesamten Antrags ist leer außer status is formally rejected",
    "dateOfSubmission": "2024-03-05T13:56:51.560Z",
    "dateLastEdited": "2024-03-05T13:56:51.560Z",
    "university": "University of Regenbogenland",
    "oldCourseOfStudy": "B.Sc Informatik",
    "newCourseOfStudy": "B.Sc Informatik"
  },
  "moduleFormsData": [
    {
      "frontend_key": 0,
      "modulesStudent": [
        {
          "frontend_key": 0,
          "approval": "angenommen",
          "reason": "ja das kann man so machen passt alles soweit.",
          "number": "420",
          "title": "AlgoDat 1.5",
          "credits": "5",
          "university": "University of Regenbogenland",
          "major": "B.Sc. Informatik",
          "commentStudent": "War cool"
        },
        {
          "frontend_key": 1,
          "approval": "angenommen",
          "reason": "ja das kann man so machen passt alles soweit.",
          "number": "4202",
          "title": "AlgoDat 0.5",
          "credits": "5",
          "university": "University of Regenbogenland",
          "major": "B.Sc. Informatik",
          "commentStudent": "War easy"
        }
      ],
      "modules2bCredited": [
        {
          "name": "Einführung in die Objectorientierte Programmierung",
          "number": "123-231-213-1"
        },
        {
          "name": "Programmierparadigmen",
          "number": "123-231-213-2"
        }
      ]
    },
    {
      "frontend_key": 1,
      "modulesStudent": [
        {
          "frontend_key": 0,
          "approval": "",
          "reason": "bin mir noch unsicher deshalb noch kein approval",
          "number": "81923",
          "title": "Das Alles-Modul",
          "credits": "20",
          "university": "University of Regenbogenland",
          "major": "B. Sc. Informatik",
          "commentStudent": "Die Beschreibung ist lang, setzen Sie schon mal Kaffee auf"
        }
      ],
      "modules2bCredited": [
        {
          "name": "Grundlagen der Technischen Informatik 1",
          "number": "123-231-213-3"
        },
        {
          "name": "Analysis",
          "number": "123-231-213-4"
        }
      ]
    }
  ]
}

```

</details>

### Get PDF Summary

<details>
<summary>Click to expand Get PDF Summary endpoint details</summary>

#### Endpoint
`GET /api/student/getPdfSummary`

#### Description
The frontend sends the application ID, and the backend responds with the corresponding PDF summary of the application.

#### Request Body
```http request
    /getPdfSummary?applicationId=1234-1234
```

#### Response Body
```text
   byte[]
```

</details>

### Get University Modules

<details>
<summary>Click to expand Get University Modules endpoint details</summary>

#### Endpoint
`GET /api/unidata/getModules`

#### Description
The frontend sends the study program, and the backend provides the modules to be credited for this study program.

#### Request Body
```http request
http://localhost:3000/api/unidata/getModules?majorName=B.Sc. Informatik
```

#### Response Body
```json
{
  "name": "B.Sc. Informatik",
  "modules": [
    {
      "number": "10-201-2005-2",
      "name": "Programmierparadigmen",
      "id": 2
    },
    {
      "number": "10-201-2001-1",
      "name": "Algorithmen und Datenstrukturen 1",
      "id": 3
    }
  ]
}
```

</details>

### Get University Study Programs

<details>
<summary>Click to expand Get University Study Programs endpoint details</summary>

#### Endpoint
`GET /api/unidata/getMajors`

#### Description
The frontend receives a JSON with study programs that can be selected.

#### Request Body
```json

```

#### Response Body
```json
{
  "courses": [
    {
      "name": "B.Sc. Informatik"
    },
    {
      "name": "B.Sc. Bio-Informatik"
    }
  ]
}
```

</details>

## Secured Endpoints

### Office Save Edited Application

<details>
<summary>Click to expand Office Save Edited Application endpoint details</summary>

#### Endpoint
`PUT /api/application/saveEdited`

#### Description
Allows office employees to save their changes to an application.

#### Request Body
```json
{
  "original": {
    "applicationData": {
      "applicationID": "23-23-23-23-23-23-23-23-23-23-23",
      "status": "",
      "formalReject": "JO du hast das völlig falsch gemacht mach den antrag nochmal",
      "dateOfSubmission": "2024-03-05T13:56:51.560Z",
      "dateLastEdited": "2024-03-05T13:56:51.560Z",
      "university": "University of Regenbogenland",
      "oldCourseOfStudy": "B.Sc Informatik",       
      "newCourseOfStudy": "B.Sc Informatik"
    },
    "moduleFormsData": [
      {
        "frontend_key": 0,
        "backend_block_id": 1,
        "modulesStudent": [
          {
            "frontend_key": 0,
            "backend_module_id": 3,
            "approval": "angenommen",
            "reason": "ja das kann man so machen passt alles soweit.",
            "number": "420",
            "title": "AlgoDat 1.5",
            "path": "/23-23-23-23-23-23-23-23-23-23-23/S-3",
            "credits": "5",
            "university": "University of Regenbogenland",
            "major": "B.Sc. Informatik",
            "commentStudent": "War cool",
            "commentEmployee": "Das nicht so cool"
          },
          {
            "frontend_key": 1,
            "backend_module_id": 5,
            "approval": "angenommen",
            "reason": "ja das kann man so machen passt alles soweit.",
            "number": "4202",
            "title": "AlgoDat 0.5",
            "path": "/23-23-23-23-23-23-23-23-23-23-23/S-5",
            "credits": "5",
            "university": "University of Regenbogenland",
            "major": "B.Sc. Informatik",
            "commentStudent": "War easy",
            "commentEmployee": "Das nicht so cool"
          }
        ],
        "modules2bCredited": [
          1,
          2
        ]
      },
      {
        "frontend_key": 1,
        "backend_block_id": 3,
        "modulesStudent": [
          {
            "frontend_key": 0,
            "backend_module_id": 5,
            "approval": "angenommen",
            "reason": "ja das kann man so machen passt alles soweit.",
            "number": "81923",
            "title": "Das Alles-Modul",
            "path": "/23-23-23-23-23-23-23-23-23-23-23/S-5",
            "credits": "20",
            "university": "University of Regenbogenland",
            "major": "B. Sc. Informatik",
            "commentStudent": "Die Beschreibung ist lang, setzen Sie schon mal Kaffee auf",
            "commentEmployee": "Beschreibung war wirklich lang, Kaffee ist alle"
          }
        ],
        "modules2bCredited": [
          5,
          8
        ]
      }
    ]
  },
  "edited": {
    "applicationData": {
      "applicationID": "23-23-23-23-23-23-23-23-23-23-23",
      "status": "",
      "formalReject": "JO du hast das völlig falsch gemacht mach den antrag nochmal",
      "dateOfSubmission": "2024-03-05T13:56:51.560Z",
      "dateLastEdited": "2024-03-05T13:56:51.560Z",
      "university": "University of Regenbogenland",
      "oldCourseOfStudy": "B.Sc Informatik",      
      "newCourseOfStudy": "B.Sc Informatik"  

    },
    "moduleFormsData": [
      {
        "frontend_key": 0,
        "backend_block_id": 1,
        "modulesStudent": [
          {
            "frontend_key": 0,
            "backend_module_id": 3,
            "approval": "angenommen",
            "reason": "ja das kann man so machen passt alles soweit.",
            "number": "420",
            "title": "AlgoDat 1.5",
            "path": "/23-23-23-23-23-23-23-23-23-23-23/S-3",
            "credits": "5",
            "university": "University of Regenbogenland",
            "major": "B.Sc. Informatik",
            "commentStudent": "War cool",
            "commentEmployee": "Das nicht so cool"
          },
          {
            "frontend_key": 1,
            "backend_module_id": 5,
            "approval": "angenommen",
            "reason": "ja das kann man so machen passt alles soweit.",
            "number": "4202",
            "title": "AlgoDat 0.5",
            "path": "/23-23-23-23-23-23-23-23-23-23-23/S-5",
            "credits": "5",
            "university": "University of Regenbogenland",
            "major": "B.Sc. Informatik",
            "commentStudent": "War easy",
            "commentEmployee": "Das nicht so cool"
          }
        ],
        "modules2bCredited": [
          1,
          2
        ]
      },
      {
        "frontend_key": 1,
        "backend_block_id": 3,
        "modulesStudent": [
          {
            "frontend_key": 0,
            "backend_module_id": 5,
            "approval": "angenommen",
            "reason": "ja das kann man so machen passt alles soweit.",
            "number": "81923",
            "title": "Das Alles-Modul",
            "path": "/23-23-23-23-23-23-23-23-23-23-23/S-5",
            "credits": "20",
            "university": "University of Regenbogenland",
            "major": "B. Sc. Informatik",
            "commentStudent": "Die Beschreibung ist lang, setzen Sie schon mal Kaffee auf",
            "commentEmployee": "Beschreibung war wirklich lang, Kaffee ist alle"
          }
        ],
        "modules2bCredited": [
          5,
          8
        ]
      }
    ]
  }
}
```

#### Response Body
```text
    201 Http.Created
```

</details>


### Application Ready for Approval

<details>
<summary>Click to expand Application Ready for Approval endpoint details</summary>

#### Endpoint
`PUT /api/application/readyForApproval`

#### Description
Marks an application as ready to be reviewed by the committee. This may involve saving the edited application again and setting the status to "ReadyForApproval."

#### Request Body
```text
    same Json as in saveEdited
```

#### Response Body
```text
    200 Ok
```

</details>

### Committee Save Approval

<details>
<summary>Click to expand Committee Save Approval endpoint details</summary>

#### Endpoint
`PUT /api/application/saveApproval`

#### Description
Provides a dedicated endpoint for the committee to save an edited application, allowing backend access for rejecting and accepting modules/applications only to committee members.

#### Request Body
```text
   same Json as in saveEdited
```

#### Response Body
```text
   200 ok
```

</details>

### Paging, Filtering, Sorting Application Overview

<details>
<summary>Click to expand Paging, Filtering, Sorting Application Overview endpoint details</summary>

#### Endpoint
`GET /api/application/overviewOffice` and `GET /api/application/overviewCommittee`

#### Description
The frontend specifies sorting and whether ascending or descending, and the backend provides a list of applications with overview details (not the entire applications). The endpoint depends on the backend implementation of pagination, filtering, and sorting.

</details>

### Find Application to Compare

<details>
<summary>Click to expand Find Application to Compare endpoint details</summary>

#### Endpoint
`GET /api/application/searchApplication`

#### Description
Searches the entire database to find applications that can be compared to current ones based on specified criteria.

#### Request Body
```http request
    /searchApplication?sortBy=dateOfSubmission&moduleName=Algo
```

#### Response Body
```json
    {
  "content": [
    {
      "applicationID": "563d7657-d457-49ad-a8e2-2fe25b7f0656",
      "university": "University of Regenbogenland",
      "dateOfSubmission": "2023-12-31T22:30:42.103Z",
      "dateLastEdited": "2023-12-31T22:30:42.103Z",
      "status": "approval finished"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 5,
    "sort": {
      "sorted": true,
      "empty": false,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 1,
  "size": 5,
  "number": 0,
  "sort": {
    "sorted": true,
    "empty": false,
    "unsorted": false
  },
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
```

</details>

### Get Single Application

<details>
<summary>Click to expand Get Single Application endpoint details</summary>

#### Endpoint
`GET /api/application/getApplication`

#### Description
The frontend sends the application ID, and the backend provides the application JSON with all details for the committee and office to compare, edit, or approve.

#### Request Body
```http request
   /getApplication?applicationID=1234-1234
```

#### Response Body
```text
    same as saveEdited
```

</details>

### Get PDF of Module

<details>
<summary>Click to expand Get PDF of Module endpoint details</summary>

#### Endpoint
`GET /api/application/getModulePDF`

#### Description
The frontend sends the application ID and module ID, and the backend sends module details and the PDF module description.

#### Request Body
```http request
    /getModulePDF?filePath=/1234-1234/S-3
```

#### Response Body
```text
    byte[]
```

</details>

### Set University Study Programs

<details>
<summary>Click to expand Set University Study Programs endpoint details</summary>

#### Endpoint
`POST /api/unidata/update`

#### Description
The frontend provides a JSON with all study programs and their modules. The backend stores them in the database.

#### Request Body
```json
{
  "courses": [
    {
      "name": "B.Sc. Informatik",
      "modules" : [
        {
          "name": "Programmierparadigmen",
          "number": "10-201-2005-2"
        },
        {
          "name": "Algorithmen und Datenstrukturen 1",
          "number": "10-201-2001-1"
        }
      ]
    },
    {
      "name": "B.Sc. Bio-Informatik",
      "modules" : [
        {
          "name": "Evolutiontheorie",
          "number": "9-144-1024-4"
        }
      ]
    }
  ]
}
```

#### Response Body
```text
    200 HTTP ok response
```

</details>

### Employee Get ALL University Modules and their Visibility

<details>
<summary>Click to expand Get University Modules endpoint details</summary>

#### Endpoint
`GET /api/unidata/getAllModules`

#### Description
The frontend sends the study program, and the backend provides visible AND invisible modules for this study program.

#### Request Body
```http request
http://localhost:3000/api/unidata/getAllModules?majorName=B.Sc. Informatik
```

#### Response Body
```json
{
  "name": "B.Sc. Informatik",
  "visibleForStudents": true,
  "modules": [
    {
      "number": "10-201-2012",
      "name": "Einführung in die objektorientierte Modellierung und Programmierung",
      "id": 1,
      "visibleForStudents": false
    },
    {
      "number": "10-201-2005-2",
      "name": "Programmierparadigmen",
      "id": 2,
      "visibleForStudents": true
    }
  ]
}
```

</details>

### Employee Get ALL University Study Programs

<details>
<summary>Click to expand Get University Study Programs endpoint details</summary>

#### Endpoint
`GET /api/unidata/getAllMajors`

#### Description
The frontend receives a JSON with ALL study programs and their Visibilly.

#### Request Body
```text
-
```

#### Response Body
```json
{
  "courses": [
    {
      "name": "M.Sc. Informatik",
      "visibleForStudents": false
    },
    {
      "name": "B.Sc. Informatik",
      "visibleForStudents": true
    }
  ]
}
```

</details>



## Additional Notes

- All endpoints should return appropriate HTTP status codes to indicate success or failure.
- The frontend team should handle errors gracefully and inform the user accordingly.
- The backend team should ensure that all endpoints are secure and that data validation is performed to prevent malicious attacks.

Please update the placeholders with the specific request and response formats as they are defined and implemented.
```
