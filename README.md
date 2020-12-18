# LibraryApp
This is a small (but very involved) Android App that is based on
[**Library App**](https://github.com/theozgurr/LibraryApp) developed by
**Ozgur Tas**. Although it is a simple App, however, it demonstrates
many technologies in one app such as MVVM pattern, RecyclerView, Dagger
2, Data Binding, Kotlin Coroutines, Room DB, and LiveData. As
demonstrated, my version does what Mr. Tas implemented, however, I've
done the following improvements:

- removed the repository layer and replaced it with a Business Object layer. In the near future, I will upgrade it to function more like [JBoss Hibernate](https://docs.jboss.org/hibernate/orm/current/quickstart/html_single/) (a popular ORM framework)
- the DAO layer has been made to be more generic
- simplified Dagger 2 integration, and removed several unneeded classes
- Initial DB is no longer hard coded, and it's now retrieved at startup via Restful webservices using **Retrofit 2**
- The app subscribes to **MQTT Broker** and listens to incoming messages to execute CRUD operations, like adding and deleting books & categories.

![Main Image](https://raw.githubusercontent.com/abusous2000/LibraryApp/master/MainImage.png)


Let me know what you think guys
Peace,

Abu al-Sous
