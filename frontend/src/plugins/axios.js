// axios.js

import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';




function generateRandomApplication() {
    return {
        "applicationKeyClass": {
            "id": Math.random().toString(36).substr(2, 9),
            "creator": "Employee"
        },
        "status": ["open", "pending", "approved", "rejected", "under review"][Math.floor(Math.random() * 5)],
        "dateOfSubmission": new Date(2023, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)).toISOString(),
        "dateLastEdited": new Date(2024, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)).toISOString(),
        "universityName": "Random University",
        "studentMajor": "B. Sc. Random Major",
        "uniMajor": "B.Sc. Random Major",
        "formalRejectionReason": Math.random() < 0.5 ? "Random Reason" : ""
    };
}


function generateDetailedApplicationWithId(applicationId){
   
    return {
      "original": {
        "applicationData": {
          "applicationID": applicationId,
          "status": "open",
          "formalReject": "",
          "dateOfSubmission": new Date().toISOString(),
          "dateLastEdited": new Date().toISOString(),
          "university": "jojojo was geht ab",
          "oldCourseOfStudy": "original",
          "newCourseOfStudy": "original"
        },
        "moduleFormsData": [
            {
                "frontend_key": 0,
                "backend_block_id": 1,
                "modulesStudent": [
                  {
                    "frontend_key": 0,
                    "backend_module_id": 1,
                    "approval": "",
                    "reason": "",
                    "number": "original",
                    "title": "original",
                    "path": "/path",
                    "credits": 5,
                    "university": "original",
                    "major": "original",
                    "commentStudent": "original",
                    "commentEmployee": ""
                  },
                  {
                    "frontend_key": 1,
                    "backend_module_id": 3,
                    "approval": "",
                    "reason": "",
                    "number": "original",
                    "title": "original",
                    "path": "/path",
                    "credits": 5,
                    "university": "original",
                    "major": "original",
                    "commentStudent": "original",
                    "commentEmployee": ""
                  }
        ],    "modules2bCredited": [{
            "name": "Einführung in die Objectorientierte Programmierung",
            "number": "123-231-213-1"
        }
          ]
        },
        {
          "frontend_key": 1,
          "backend_block_id": 3,
          "modulesStudent": [
            {
              "frontend_key": 0,
              "backend_module_id": 5,
              "approval": "",
              "reason": "",
              "number": "original",
              "title": "original",
              "path": "/path",
              "credits": 5,
              "university": "original",
              "major": "original",
              "commentStudent": "original",
              "commentEmployee": ""
            }
          ],
          "modules2bCredited": [{
            "name": "Einführung in die Objectorientierte Programmierung",
            "number": "123-231-213-1"
        }
            
          ]
        }
      ]
    
      },
      "edited": {
        "applicationData": {
          "applicationID": applicationId,
          "status": "edited approval",
          "formalReject": "edited",
          "dateOfSubmission": new Date().toISOString(),
          "dateLastEdited": new Date().toISOString(),
          "university": "edited",
          "oldCourseOfStudy": "edited",
          "newCourseOfStudy": "original"
        },
        "moduleFormsData": [
          // Populate with your moduleFormsData objects
        ]
      }
    };
  }
  
  

// Create an Axios instance with custom configuration
const instance = axios.create({
    baseURL: process.env.VUE_APP_API_URL,
    timeout: 10000, // Timeout in milliseconds
    headers: {
        'Content-Type': 'application/json'
    },
});

if (process.env.NODE_ENV === 'development') {
    const mock = new MockAdapter(instance);

    // Mock any GET request to /application/getApplication
    // Reply with status 200 and a sample response object
    mock.onGet('api/application/getApplication').reply(config => {
        // Extract the application ID from the query parameters
        const applicationId = config.params && config.params.applicationID;

        // Check if an application ID was extracted
        if (applicationId) {
            // Generate a detailed application object with the extracted ID
            return [200, generateDetailedApplicationWithId(applicationId)];
        } else {
            // No ID provided in the query parameters, return an error response
            return [400, { message: "No application ID provided" }];
        }
    });
    mock.onGet("api/application/get-all-applications").reply(200, Array.from({ length: 30 }, generateRandomApplication));
        
    // mock.onGet('api/application/getApplication').reply(200, 
    //     {
    //         "original": {
    //           "applicationData": {
    //             "applicationID": "1234-1234",
    //             "status": "open",
    //             "formalReject": "",
    //             "dateOfSubmission": "2020-03-05T13:56:51.560Z",
    //             "dateLastEdited": "2020-03-05T13:56:51.560Z",
    //             "university": "original",
    //             "oldCourseOfStudy": "original",
    //             "newCourseOfStudy": "original"
    //           },
    //           "moduleFormsData": [
    //             {
    //               "frontend_key": 0,
    //               "backend_block_id": 1,
    //               "modulesStudent": [
    //                 {
    //                   "frontend_key": 0,
    //                   "backend_module_id": 1,
    //                   "approval": "",
    //                   "reason": "",
    //                   "number": "original",
    //                   "title": "original",
    //                   "path": "/path",
    //                   "credits": 5,
    //                   "university": "original",
    //                   "major": "original",
    //                   "commentStudent": "original",
    //                   "commentEmployee": ""
    //                 },
    //                 {
    //                   "frontend_key": 1,
    //                   "backend_module_id": 3,
    //                   "approval": "",
    //                   "reason": "",
    //                   "number": "original",
    //                   "title": "original",
    //                   "path": "/path",
    //                   "credits": 5,
    //                   "university": "original",
    //                   "major": "original",
    //                   "commentStudent": "original",
    //                   "commentEmployee": ""
    //                 }
    //               ],
    //               "modules2bCredited": [
    //                 1,
    //                 2
    //               ]
    //             },
    //             {
    //               "frontend_key": 1,
    //               "backend_block_id": 3,
    //               "modulesStudent": [
    //                 {
    //                   "frontend_key": 0,
    //                   "backend_module_id": 5,
    //                   "approval": "",
    //                   "reason": "",
    //                   "number": "original",
    //                   "title": "original",
    //                   "path": "/path",
    //                   "credits": 5,
    //                   "university": "original",
    //                   "major": "original",
    //                   "commentStudent": "original",
    //                   "commentEmployee": ""
    //                 }
    //               ],
    //               "modules2bCredited": [
    //                 7
    //               ]
    //             }
    //           ]
    //         },
    //         "edited": {
    //           "applicationData": {
    //             "applicationID": "1234-1234",
    //             "status": "edited approval",
    //             "formalReject": "edited",
    //             "dateOfSubmission": "2020-03-05T13:56:51.560Z",
    //             "dateLastEdited": "2023-03-05T13:56:51.560Z",
    //             "university": "edited",
    //             "oldCourseOfStudy": "edited",
    //             "newCourseOfStudy": "original"
          
    //           },
    //           "moduleFormsData": [
    //             {
    //               "frontend_key": 0,
    //               "backend_block_id": 2,
    //               "modulesStudent": [
    //                 {
    //                   "frontend_key": 0,
    //                   "backend_module_id": 2,
    //                   "approval": "accepted",
    //                   "reason": "edited",
    //                   "number": "edited",
    //                   "title": "edited",
    //                   "path": "/path",
    //                   "credits": 0,
    //                   "university": "edited",
    //                   "major": "edited",
    //                   "commentStudent": "edited",
    //                   "commentEmployee": "edited"
    //                 },
    //                 {
    //                   "frontend_key": 1,
    //                   "backend_module_id": 6,
    //                   "approval": "rejected",
    //                   "reason": "edited",
    //                   "number": "edited",
    //                   "title": "edited",
    //                   "path": "/path",
    //                   "credits": 0,
    //                   "university": "edited",
    //                   "major": "edited",
    //                   "commentStudent": "edited",
    //                   "commentEmployee": "edited"
    //                 }
    //               ],
    //               "modules2bCredited": [
    //                 1
    //               ]
    //             },
    //             {
    //               "frontend_key": 1,
    //               "backend_block_id": 4,
    //               "modulesStudent": [
    //                 {
    //                   "frontend_key": 0,
    //                   "backend_module_id": 4,
    //                   "approval": "",
    //                   "reason": "edited",
    //                   "number": "edited",
    //                   "title": "edited",
    //                   "path": "/path",
    //                   "credits": 0,
    //                   "university": "edited",
    //                   "major": "edited",
    //                   "commentStudent": "edited",
    //                   "commentEmployee": "edited"
    //                 }
    //               ],
    //               "modules2bCredited": [
    //                 5,
    //                 6
    //               ]
    //             }
    //           ]
    //         }
    //       }
    // );

    // mock.onGet('api/application/get-all-applications').reply(200, [
    //     {
    //         "applicationKeyClass": {
    //           "id": "1234-1234",
    //           "creator": "Employee"
    //         },
    //         "status": "open",
    //         "dateOfSubmission": "2023-12-31T22:30:42.103+00:00",
    //         "dateLastEdited": "2024-01-14T14:12:14.675+00:00",
    //         "universityName": "University of Regenbogenland",
    //         "studentMajor": "B. Sc. Informatik",
    //         "uniMajor": "B.Sc. Informatik",
    //         "formalRejectionReason": ""
    //       }
    // ]);
}

// Request interceptor
instance.interceptors.request.use(
    (config) => {
        // You can modify the request config here (e.g., add authentication headers)
        return config;
    },
    (error) => {
        // Handle request error
        return Promise.reject(error);
    }
);

// Response interceptor
instance.interceptors.response.use(
    (response) => {
        // You can modify the response here (e.g., handle common errors)
        return response;
    },
    (error) => {
        // Handle response error
        return Promise.reject(error);
    }
);

export default instance;
