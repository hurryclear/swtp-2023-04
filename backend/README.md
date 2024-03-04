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
    Request placeholder
```

#### Response Body
```json
{
  "applicationNumber": "uniqueApplicationNumber"
}
```

</details>

### Student Resubmit Application

<details>
<summary>Click to expand Student Resubmit Application endpoint details</summary>

#### Endpoint
`PUT /api/student/resubmitApplication`

#### Description
Enables students to resubmit their application, for example, if their initial submission was formally rejected.

#### Request Body
```json
    Request placeholder
```

#### Response Body
```json
    Response placeholder
```

</details>

### Get Application Summary with Status

<details>
<summary>Click to expand Get Application Summary with Status endpoint details</summary>

#### Endpoint
`GET /api/student/getApplicationSummary`

#### Description
The frontend sends the application ID, and the backend provides the application details as JSON, including the current status.

#### Request Body
```json
    Request placeholder
```

#### Response Body
```json
    Response placeholder
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
```json
    Request placeholder
```

#### Response Body
```json
    Response placeholder
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
      "name": "Programmierparadigmen"
    },
    {
      "number": "10-201-2001-1",
      "name": "Algorithmen und Datenstrukturen 1"
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
-
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
    Request placeholder
```

#### Response Body
```json
    Response placeholder
```

</details>

### Application Ready for Approval

<details>
<summary>Click to expand Application Ready for Approval endpoint details</summary>

#### Endpoint
`POST /api/application/readyForApproval`

#### Description
Marks an application as ready to be reviewed by the committee. This may involve saving the edited application again and setting the status to "ReadyForApproval."

#### Request Body
```json
    Request placeholder
```

#### Response Body
```json
    Response placeholder
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
```json
    Request placeholder
```

#### Response Body
```json
    Response placeholder
```

</details>

### Finish Approval

<details>
<summary>Click to expand Finish Approval endpoint details</summary>

#### Endpoint
`POST /api/application/finishApproval`

#### Description
May not be necessary if finishing an approval is equivalent to all modules being either accepted or rejected with justifications provided.

#### Request Body
```json
    Request placeholder
```

#### Response Body
```json
    Response placeholder
```

</details>

### Status Editing Needed by OFFICE

<details>
<summary>Click to expand Status Editing Needed by OFFICE endpoint details</summary>

#### Endpoint
`PUT /api/application/statusEditingNeeded`

#### Description
The committee sends the edited application back to the office because it must be re-edited and cannot be approved yet. The frontend sends the application number, and the backend changes the status to "further editing needed."

#### Request Body
```json
    Request placeholder
```

#### Response Body
```json
    Response placeholder
```

</details>

### Paging, Filtering, Sorting Application Overview

<details>
<summary>Click to expand Paging, Filtering, Sorting Application Overview endpoint details</summary>

#### Endpoint
`GET /api/application/overviewOffice` and `GET /api/application/overviewCommittee`

#### Description
The frontend specifies sorting and whether ascending or descending, and the backend provides a list of applications with overview details (not the entire applications). The endpoint depends on the backend implementation of pagination, filtering, and sorting.

#### Request Body
```json
    Request placeholder
```

#### Response Body
```json
    Response placeholder
```

</details>

### Find Application to Compare

<details>
<summary>Click to expand Find Application to Compare endpoint details</summary>

#### Endpoint
`GET /api/application/findApplication`

#### Description
Searches the entire database to find applications that can be compared to current ones based on specified criteria.

#### Request Body
```json
    Request placeholder
```

#### Response Body
```json
    Response placeholder
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
```json
    Request placeholder
```

#### Response Body
```json
    Response placeholder
```

</details>

### Get PDF of Module

<details>
<summary>Click to expand Get PDF of Module endpoint details</summary>

#### Endpoint
`GET /api/application/getModule`

#### Description
The frontend sends the application ID and module ID, and the backend sends module details and the PDF module description.

#### Request Body
```json
    Request placeholder
```

#### Response Body
```json
    Response placeholder
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
```json
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
      "visibleForStudents": false
    },
    {
      "number": "10-201-2005-2",
      "name": "Programmierparadigmen",
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
```json
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
