package DataBase.detenuti;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Collegamento {
public static void main (String[] args) throws IOException {
		
		try {
			MongoClient mongoClient= new MongoClient("localhost", 27017);
			
			DB db= mongoClient.getDB("Carceri");
			DBCollection collection_sesso= db.getCollection("detenuti_sesso");
			DBCollection collection_regione= db.getCollection("detenuti_regione");
			DBCollection collection_stranieri= db.getCollection("detenuti_stranieri");
			JFrame frame= new MenuFrame (db, collection_sesso, collection_regione, collection_stranieri);
			//JFrame frame= new Menu(mongoClient, db, collection);
			System.out.println("Connessione stabilita");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}