# JsonPractice

07/17/2019

Image Gallery application that queries a database via http connection to receive images to populate the app's gallery.
Images are returned as Json objects. Using Google's Gson library, Json returned queries are converted to Java objects via
library's gsonToJson method. Processes known as Deserialization(json to Java Object) and Serialization (Java Object to Json) for
transfer of data via the internet are the main topic of the app.  This project, and the subject matter were part of a Android App Development course I participated in taught at the consulting agency Rave Business Solutions LLC in Philadelphia PA.

3 techniques of HTTP URL connection were implemented as practice techniques for data transfer: Two used the libraries of Volley and Retrofit and the third was the Native Async http connection methodology.
 
