workspace {

    model {
        user1 = person "Students" ""
        user2 = person "Workers" ""
        softwareSystem = softwareSystem "Software System" {
            webapp1 = container "Web App" "Loin page with necessary credentials" "Java and Springboot"{
                tags "Web App"
                user1 -> this "Loin page with necessary credentials"
                user2 -> this "Loin page with necessary credentials"
            }
            webapp2 = container "Application create" "" "Vue.js"{
                tags "Application create"
                user1 -> this "Information input, pdf file upload and application create"
                webapp1 -> this "Delivers to the applicants' web browser"
            }
            webapp3 = container "View of all applications" "" "Vue.js"{
                tags "View of all applications"
                user2 -> this "Hava a view of all applications and also be able to download each applicaiton in pdf file"
                webapp1 -> this "Delivers to the workers' web browser"
            }
            api = container "Backend API Application" "" "Java and Springboot"{
                tags "Backend API Application"
                webapp2 -> this "Makes API calls to"
                webapp3 -> this "Makes API calls to"
            }
            db = container "Database" "Stores user and workers credentials, applications' information, pdf files and etc." "Postgres"{
                tags "Database"
                api -> this "reads from and writes to" "Postgres"
            }
        }
    }

    views {
        systemContext softwareSystem "Diagram1" {

            // autoLayout
        }

        container softwareSystem "Diagram2" {
            include *
            // autoLayout
        }

        //theme default
        styles {
            element "Person" {
                shape person
                background #043E78
                color #ffffff
            }
            element "Software System" {
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
            element "Application create" {
                shape WebBrowser
                background #3F94E9
                color #ffffff
            }
            element "View of all applications" {
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