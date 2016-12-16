/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.lavanya.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.lavanya.example.com",
                ownerName = "backend.myapplication.lavanya.example.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
   /* @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }*/

    @ApiMethod(name = "insertUser")
    public Users insertUsers(Users users) throws ConflictException {
//If if is not null, then check if it exists. If yes, throw an Exception
//that it is already present
        if (users.getId() != null) {
            if (findRecord(users.getId()) != null) {
                throw new ConflictException("User id already exists,please login");
            }
        }

        ofy().save().entity(users).now();
        return users;
    }

    /* Updates*/
    @ApiMethod(name = "updateUser")
    public Users updateUsers(Users users)throws NotFoundException {
        if (findRecord(users.getId()) == null) {
            throw new NotFoundException("User Record does not exist");
        }
        ofy().save().entity(users).now();
        return users;
    }

    /*Deletes*/
    @ApiMethod(name = "removeUser")
    public void removeUsers(@com.google.api.server.spi.config.Named("id") Long id) throws NotFoundException {
        Users record = findRecord(id);
        if(record == null) {
            throw new NotFoundException("User Record does not exist");
        }
        ofy().delete().entity(record).now();
    }


    private Users findRecord(Long id) {
        return ofy().load().type(Users.class).id(id).now();
    }

    /* List of users*/

    @ApiMethod(name = "listUsers")
    public CollectionResponse<Users> listQuote(@Nullable @com.google.api.server.spi.config.Named("cursor") String cursorString,
                                               @Nullable @com.google.api.server.spi.config.Named("count") Integer count) {

        Query<Users> query = ofy().load().type(Users.class);
        if (count != null) query.limit(count);
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }
        List<Users> records = new ArrayList<Users>();
        QueryResultIterator<Users> iterator = query.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            records.add(iterator.next());
            if (count != null) {
                num++;
                if (num == count) break;
            }
        }
        //Find the next cursor
        if (cursorString != null && cursorString != "") {
            Cursor cursor = iterator.getCursor();
            if (cursor != null) {
                cursorString = cursor.toWebSafeString();
            }
        }
        return CollectionResponse.<Users>builder().setItems(records).setNextPageToken(cursorString).build();
    }
    @ApiMethod(
            name = "orderlist",
            httpMethod = ApiMethod.HttpMethod.GET)
    public List<Orders> orderlist(@javax.annotation.Nullable @Named("cursor") String cursor){
        Query<Orders> query = ofy().load().type(Orders.class).limit(1000);
        if (cursor != null)
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        QueryResultIterator<Orders> queryIterator = query.iterator();
        List<Orders> ordersList = new ArrayList<Orders>(1000);
        while (queryIterator.hasNext()) {
            ordersList.add(queryIterator.next());
        }
        return ordersList;
    }
}
