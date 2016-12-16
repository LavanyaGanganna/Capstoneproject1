package com.example.lavanya.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import javax.mail.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "ordersApi",
        version = "v1",
        resource = "orders",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.lavanya.example.com",
                ownerName = "backend.myapplication.lavanya.example.com",
                packagePath = ""
        )
)
public class OrdersEndpoint {

    private static final Logger logger = Logger.getLogger(OrdersEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Orders.class);
    }

    /**
     * Returns the {@link Orders} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Orders} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "orders/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Orders get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Orders with ID: " + id);
        Orders orders = ofy().load().type(Orders.class).id(id).now();
        if (orders == null) {
            throw new NotFoundException("Could not find Orders with ID: " + id);
        }
        return orders;
    }

    /**
     * Inserts a new {@code Orders}.
     */
    @ApiMethod(
            name = "insert",
            path = "orders",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Orders insert(Orders orders) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that orders.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(orders).now();
        logger.info("Created Orders with ID: " + orders.getId());
        sendingMail(orders.getTitle(),orders.getEmailaddr(),orders.getTotalprice());
        return ofy().load().entity(orders).now();
    }

    /**
     * Updates an existing {@code Orders}.
     *
     * @param id     the ID of the entity to be updated
     * @param orders the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Orders}
     */
    @ApiMethod(
            name = "update",
            path = "orders/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Orders update(@Named("id") Long id, Orders orders) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(orders).now();
        logger.info("Updated Orders: " + orders);
        return ofy().load().entity(orders).now();
    }

    /**
     * Deletes the specified {@code Orders}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Orders}
     */
    @ApiMethod(
            name = "remove",
            path = "orders/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Orders.class).id(id).now();
        logger.info("Deleted Orders with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "orders",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Orders> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Orders> query = ofy().load().type(Orders.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Orders> queryIterator = query.iterator();
        List<Orders> ordersList = new ArrayList<Orders>(limit);
        while (queryIterator.hasNext()) {
            ordersList.add(queryIterator.next());
        }
        return CollectionResponse.<Orders>builder().setItems(ordersList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Orders.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Orders with ID: " + id);
        }
    }

    @ApiMethod(
            name = "checkorder",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Orders checkorder(@Named("emailaddr") String emailaddr) throws NotFoundException {
        return ofy().load().type(Orders.class).filter("emailaddr", emailaddr).first().now();
    }



    private void sendingMail(List<String> titles,String emails,String totals) {
        String body="";
        try {
            for(String title: titles) {
                body= body+title + "\n";
            }
            body=body +"\n"+ "Total cost:" +totals;
            GmailSender sender = new GmailSender("lavanyabg@gmail.com", "Mannam2016");
            logger.info("inside sending mail");
            sender.sendMail("Thank You",
                    body,
                    "lavanyabg@gmail.com",
                    emails);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}