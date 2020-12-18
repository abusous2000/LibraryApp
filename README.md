# LibraryApp
This is a small (but involved) App that is based on [Library App](https://github.com/theozgurr/LibraryApp) that was developed by **Ozgur Tas**
It does what Mr Tas implemented, however, I've done the following improvements:

- removed the repository layer and replaced with a Business Object layer. In the near future, I will upgrade it to function like JBose's Hibernate (ORM layer)
- the DAO layer has been made to be more generic
- simplified Dagger 2 integration, and removed several unneeded classes
- Initial DB is no longer hard coded, and it's now retrieved at startup via Restful webservices using **Retrofit 2**
- The app subscribes to **MQTT Broker** and listens to incoming messages to execute CRUD operations, like adding and deleting books & categories.

Let me know what you think guys
Peace,

Abu a-Sous
