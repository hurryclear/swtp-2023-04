tim uni, [23.11.23 17:25]
workspace {

    model {
        user1 = person "Studenten" "Users of software system, who makes applications"
        user2 = person "Uni Personal (SB & PAV)" "Users of software system, who works with applications"

        // Software systme "Online Plattform" for creating applications
        softwareSystem1 = softwareSystem "Online Plattform" "Students can make applications, workers can have a view of all applications and get it as pdf file" {
            tags "Online Plattform"

            // connections between users and Online Plattform
            user1 -> this "Uses"
            user2 -> this "Uses"


            webapp1 = container "Web App" "Login page with necessary credentials" "Java and Springboot"{
                tags "Web App"
                user1 -> this "Visits URL using" "HTTPS"
                user2 -> this "Visits URL using" "HTTPS"
            }
            webapp2 = container "Application Generator" "Studenten können ihre Eckdaten hier einfügen und einen Antrag erstellen" "Vue.js"{
                tags "Application generate"
                user1 -> this "Information input, pdf file upload and application create"
                webapp1 -> this "Delivers to the applicants' web browser"

            }
            webapp3 = container "View of all applications" "Mitarbeiter können hier alle Anträge sehen nd sie als pdf herunterladen." "Vue.js"{
                tags "View of all applications"
                user2 -> this "Have a view of all applications and also be able to download each application in pdf file"
                webapp1 -> this "Delivers to the workers' web browser"
            }

            // Container: Single Web Page "Login Page"
            webapp4 = container "Login Page" "All users will login in this page" "Vue.js" {
                tags "Login Page"
                user1 -> this "Login with necessary credential"
                user2 -> this "Login with necessary credential"

                
                webapp1 -> this "Delivers to the users' web browser"
            }

            // Container: Backend API Application
            api = container "Backend API Application" "Provides Online Plattform functionality via a JSON/HTTPS API." "Java and Springboot"{
                tags "Backend API Application"
                webapp2 -> this "Makes API calls to"
                webapp4 -> this "Makes API calls to"

                // components in Backend API Application
                c1 = component "Login Controller" "Allow users to sign in" "Node.js" {
                    
                    webapp4 -> this "Makes API calls to"
                }
                c2 = component "Application Number Generator" "Generate randomly but unique application numbers for each new application" "Node.js" {
                    webapp2 -> this "Makes API calls to"
                    webapp4 -> this "Makes API calls to"
                }
                c3 = component "PDF File Component" "Generate pdf file with all information from applicants" "I don't know"{
                    webapp2 -> this "Makes API calls to"
                    webapp3 -> this "Makes API calls to"
                }
                c4 = component "Security Component" "Provides functionality related to signing in, changing passwords, etc."{
                    c1 -> this "Uses"
                    
                }
            }

            db = container "Database" "Stores user and workers credentials, applications' information, pdf files and etc." "Postgres"{
                tags "Database"
                api -> this "reads from and writes to" "Postgres"
                c4 -> this "Reads from and writes to" "Postgres"
            }
        }

        
    }

    views {
        systemContext softwareSystem1 "Diagram1"{
            include *
            //autoLayout
        }
        container softwareSystem1 "Diagram2" {
            include *
            //autoLayout
        }
        component api {
            include *
            //autoLayout
        }

	//change theme (you can also just use "theme default")
        styles {
            element "Person" {
                shape person
                background #043E78
                color #ffffff
            }
            element "Online Plattform" {
                background #0266CA
                color #ffffff
            }
            element "Database" {
                shape cylinder
                background #3F94E9
                color #ffffff
            }
            element "Web App" {
                background #0465C7
                color #ffffff
            }
            element "Application generate" {
                shape WebBrowser
                background #3F94E9
                color #ffffff
            }
            element "View of all applications" {
                shape WebBrowser
                background #3F94E9
                color #ffffff
            }

            element "Login Page" {
                shape WebBrowser
                background #3F94E9
                color #ffffff
            }

            element "Backend API Application" {
                background #3F94E9
                color #ffffff
            }
        }
    }
}
