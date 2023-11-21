workspace {

    model {
        user1 = person "Students" "Users of software system, who makes applications"
        user2 = person "Workers" "Users of software system, who works with applications"

        // Software systme "Online Plattform" for creating applications
        softwareSystem1 = softwareSystem "Online Plattform" "Students can make applications, workers can have a view of all applications and get it as pdf file" {
            tags "Online Plattform"

            // connections between users and Online Plattform
            user1 -> this "Uses"
            user2 -> this "Uses"


            webapp1 = container "Web App" "Loin page with necessary credentials" "Java and Springboot"{
                tags "Web App"
                user1 -> this "Loin page with necessary credentials"
                user2 -> this "Loin page with necessary credentials"
            }
            webapp2 = container "Application generate" "Students can fill application informations in this page and generate complete applications" "Vue.js"{
                tags "Application generate"
                user1 -> this "Information input, pdf file upload and application create"
                webapp1 -> this "Delivers to the applicants' web browser"

            }
            webapp3 = container "View of all applications" "Workers can have a view of all applications and download pdf file of application." "Vue.js"{
                tags "View of all applications"
                user2 -> this "Hava a view of all applications and also be able to download each applicaiton in pdf file"
                webapp1 -> this "Delivers to the workers' web browser"
            }

            webapp4 = container "Login Page" "All users will login in this page" "Vue.js" {
                tags "Login Page"
                user1 -> this "Login with necessary credential"
                user2 -> this "Login with necessary credential"
                webapp1 -> this "Delivers to the users' web browser"
            }

            api = container "Backend API Application" "Provides Online Plattform functionally via a JSON/HTTPS API." "Java and Springboot"{
                tags "Backend API Application"
                webapp2 -> this "Makes API calls to"
                webapp3 -> this "Makes API calls to"
                webapp4 -> this "Makes API calls to"
            }
            db = container "Database" "Stores user and workers credentials, applications' information, pdf files and etc." "Postgres"{
                tags "Database"
                api -> this "reads from and writes to" "Postgres"
            }
        }

        // Software system "Worker's Working System", the area for workers to work with applications
        softwareSystem2 = softwareSystem "Worker's Working System" "Workers work with applications in this system" {
            tags "Worker's Working System"
            user2 -> this "works in"
        }

        softwareSystem1 -> softwareSystem2 "generate applications and send it as pdf file"
        softwareSystem2 -> softwareSystem1 "send results of applications back"

    }

    views {
        systemContext softwareSystem1 "Diagram1"{
            include *
            //autoLayout
        }
        systemContext softwareSystem2 "Diagram3" {
            include *
        }
        container softwareSystem1 "Diagram2" {
            include *
            //autoLayout
        }


        //theme default
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
            element "Worker's Working System" {
                background #999DA0
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