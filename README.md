# plivo-contact-book-app documentation

App is live at : https://contact-book-app-plivo.herokuapp.com/ (might take time to reload because app is getting UP)

Swagger Page : https://contact-book-app-plivo.herokuapp.com/swagger-ui.html (This is only meant to help with documentation of API's(give a feel of UI) & not testing, for Testing you can use any tool like Postman)

Basic Authentication credentials : username:sarthak password:plivo#123 (without these you cannot access the API's)

CRUD REST API's:
1. GET https://contact-book-app-plivo.herokuapp.com/api/contact-book/v1/contacts (to get all contacts,Pagination enabled)

2. POST https://contact-book-app-plivo.herokuapp.com//api/contact-book/v1/insert (Insert a contact)
    sample request body:                                          
	               { "name":"Sarthak",
	                "phoneNumber":"8829055002",         
	                "emailId":"sarthak1234@gmail.com",
	                "city":"Delhi"}
                  
    sample response body:
                         { "id": 132,
                          "name": "Sarthak",
                          "phoneNumber": "8829055002",
                          "emailId": "sarthak12@gmail.com",
                          "city": "Delhi"}
3. PUT https://contact-book-app-plivo.herokuapp.com//api/contact-book/v1/contacts/{id} -> mention id here to update   
       Ex: https://contact-book-app-plivo.herokuapp.com//api/contact-book/v1/contacts/132
       Note: Updation of a single field as well as all the fields is possible
       sample request body1:(updates all mentioned fields)
          {"name": "Ajay",
          "phoneNumber": "8829055002",
          "emailId": "sarthak12@gmail.com",
          "city": "Delhi"}
       sample request body2:(updates a single field for a contact here)
          {"name": "rajat"}
    
4. DELETE https://contact-book-app-plivo.herokuapp.com/api/contact-book/v1/contacts/{id} -> mention id here to delete
           Ex: https://contact-book-app-plivo.herokuapp.com//api/contact-book/v1/contacts/52 

5. GET https://contact-book-app-plivo.herokuapp.com/api/contact-book/v1/getContactsByName?name=vijay (Params -> name) Searching by name(Pagination enabled)

6. GET https://contact-book-app-plivo.herokuapp.com/api/contact-book/v1/getContactsByEmail?email=vijay@gmail.com (Params -> email)
Searching by email(Pagination enabled)

7.GET https://contact-book-app-plivo.herokuapp.com/api/contact-book/v1/getContactsByNameAndEmail?name=vijay&email=vijay@gmail.com (Params-> name & email)
Searching by name and email(Pagination enabled)

Note: These operation are able to maintain unique email for each contact.
      Database: MYSQL
      If API gives 500 server error , please check the params and manipulate your request to add the required params.
      (Sample pagination request: https://contact-book-app-plivo.herokuapp.com/api/contact-book/v1/contacts?page=0&size =4)


    

