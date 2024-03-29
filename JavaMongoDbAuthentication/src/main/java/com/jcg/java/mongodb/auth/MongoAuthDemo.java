package com.jcg.java.mongodb.auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoAuthDemo {

	private static Logger log = Logger.getLogger(MongoAuthDemo.class);

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		// Mongodb initialization parameters.
		int port_no = 27017;
		String auth_user="jcg", auth_pwd = "admin@123", host_name = "localhost", db_name = "mongoauthdemo", db_col_name = "emp", encoded_pwd = "";

		/* Imp. Note - 
		 * 		1.	Developers will need to encode the 'auth_user' or the 'auth_pwd' string if it contains the <code>:</code> or the <code>@</code> symbol. If not, the code will throw the <code>java.lang.IllegalArgumentException</code>.
		 *		2.	If the 'auth_user' or the 'auth_pwd' string does not contain the <code>:</code> or the <code>@</code> symbol, we can skip the encoding step.
		 */
		try {
			encoded_pwd = URLEncoder.encode(auth_pwd, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			log.error(ex);
		}

		// Mongodb connection string.
		String client_url = "mongodb://" + auth_user + ":" + encoded_pwd + "@" + host_name + ":" + port_no + "/" + db_name;
		MongoClientURI uri = new MongoClientURI(client_url);

		// Connecting to the mongodb server using the given client uri.
		MongoClient mongo_client = new MongoClient(uri);

		// Fetching the database from the mongodb.
		MongoDatabase db = mongo_client.getDatabase(db_name);

		// Fetching the collection from the mongodb.
		MongoCollection<Document> coll = db.getCollection(db_col_name);
		log.info("Fetching all documents from the collection");

		// Performing a read operation on the collection.
		FindIterable<Document> fi = coll.find();
		MongoCursor<Document> cursor = fi.iterator();
		try {
			while(cursor.hasNext()) {
				log.info(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}
	}
}