# Make appointment in vet clinic

## GET method /{lastname}
* lastname - doctors lastname who appointments you want see
* body: 

         {
              "date" : "yyyy-mm-dd"
          }           
   
## POST method /{identifier}/{pin}/{lastname}
* identifier - user unique identifier to make or cancel appointment (4 digits)
* pin - user pin number to make or cancel appointment(4 digits)
* lastname - doctors lastname who you want to make appointment
* body: 

         {
              "date" : "yyyy-mm-dd",
              "startTime" : "hh-mm-ss"
          } 
          
 ## DELETE method /{identifier}/{pin}
 * identifier - user unique identifier to make or cancel appointment (4 digits)
 * pin - user pin number to make or cancel appointment(4 digits)
 * body: 

         {
              "date" : "yyyy-mm-dd",
              "startTime" : "hh-mm-ss"
          }
