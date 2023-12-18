workspace {

    model {
        user1 = person "Student" "Nutzer der App, sie verfassen Anträge"
        user2 = person "Studienbüro" "Nutzer der App, sie verarbeiten Antrtäge"
        user3 = person "Prüfungsausschuss" "Nutzer der App, sie verarbeiten Anträge"
 
        // Software systme "Online Plattform" for creating applications
        softwareSystem1 = softwareSystem "Online Plattform" "Studenten können Anträge machen, SB & PAV können alle Anträge anschauen und als pdf herunterladen." {
            tags "Online Plattform"

            // connections between users and Online Plattform
            user1 -> this "Uses"
            user2 -> this "Uses"
            user3 -> this "Uses"

            
            webapp2 = container "Antragsseite" "Studenten können ihre Eckdaten hier einfügen und einen Antrag erstellen." "Vue.js"{
                tags "Application generate"
                user1 -> this "Information input, pdf file upload and application create"
                

            }
            webapp3 = container "View of all applications" "Mitarbeiter können hier alle Anträge sehen nd sie als pdf herunterladen." "Vue.js"{
                tags "View of all applications"
                user2 -> this "Have a view of all applications and also be able to download each application in pdf file"
                user3 -> this "Have a view of all applications and also be able to download each application in pdf file"
                
            }

            // Container: Single Web Page "Login Page"
            webapp4 = container "Login Page" "Alle Nutzer loggen sich hier ein" "Vue.js" {
                tags "Login Page"
                user1 -> this "Login with necessary credential"
                user2 -> this "Login with necessary credential"
                user3 -> this "Login with necessary credential"
                
            }
            
            webapp5 = container "Review page" "Studenten können ihre Eckdaten hier einfügen und einen Antrag erstellen." "Vue.js"{
                tags "Application generate"
                user1 -> this "Uses"
                
            }


            
           
            
            

            // Container: Backend API Application
            api = container "Backend API Application" "Sorgt für die Funktionalität der App via JSON/HTTPS API." "Java and Springboot"{
                tags "Backend API Application"
                webapp2 -> this "Sends Application Infos"
                webapp4 -> this "Requests login credentials"
                webapp5 -> this "Requests pdf-summary"
                webapp3 -> this "Requests Lists and content of Applications"
                // components in Backend API Application
                c1 = component "Controller" "Ermöglicht das einloggen." "Node.js" {
                    webapp2 -> this "Sends Application Infos"
                    webapp4 -> this "Requests login credentials"
                    webapp5 -> this "Requests pdf-summary"
                    webapp3 -> this "Requests Lists and content of Applications"
                }
                c3 = component "PDF Generator" "Generiert pdf Datei mit Eckdaten der Antragssteller" "I don't know"{
                    c1 -> this "requests pdf-file"
                }
                c2 = component "Application Number Generator" "Generiert zufällige, aber eindeutige Antragsnummern für jeden neuen Antrag." "Node.js" {
                    c1 -> this "requests Application Number"
                    
                }
                c4 = component "JSON to Database converter" "Extrahiert Daten aus JSON-File aus dem Frontend zum speichern in der Datenbank" "Node.js" {
                    c1 -> this "requests JSON File"
                    
                }
                
                
            }

            db = container "Database" "Speichert user credentials, Antragsinformationen, pdf dateien etc." "Postgres"{
                tags "Database"
                api -> this "reads from and writes to" "Postgres"
                c1 -> this "Reads from and writes to" "Postgres"
               
                
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
