package com.example.sequence_1_rames_leo.Autres

import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import com.example.sequence_1_rames_leo.Activitys.MainActivity
import com.example.sequence_1_rames_leo.Activitys.MainActivity.Companion.AddItem
import com.example.sequence_1_rames_leo.Activitys.MainActivity.Companion.AddList
import com.example.sequence_1_rames_leo.Activitys.MainActivity.Companion.CheckItem
import com.example.sequence_1_rames_leo.Activitys.MainActivity.Companion.DelItem
import com.example.sequence_1_rames_leo.Activitys.MainActivity.Companion.DelList
import com.example.sequence_1_rames_leo.Activitys.MainActivity.Companion.GetItems
import com.example.sequence_1_rames_leo.Activitys.MainActivity.Companion.LOG
import com.example.sequence_1_rames_leo.Activitys.MainActivity.Companion.SwitchState
import com.example.sequence_1_rames_leo.Activitys.MainActivity.Companion.UnCheckItem
import okhttp3.*
import org.json.JSONObject


class MyAsyncTask : AsyncTask<String, Void, String>() {
    private lateinit  var sp : SharedPreferences
    private lateinit  var  editor: SharedPreferences.Editor

    init{
        sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.Companion.context)
        editor = sp.edit();
    }




    fun addListtoHash(titre: String): Boolean {
        try{
            val client = OkHttpClient().newBuilder()
                .build()
            val mediaType = MediaType.parse("text/plain")
            val body = RequestBody.create(mediaType, "")
            val request: Request = Request.Builder()
                .url("http://tomnab.fr/todo-api/lists?label=$titre")
                .method("POST", body)
                .addHeader("hash", DataProvider.getUser().HASH)
                .build()
            val response = client.newCall(request).execute()
            return response.isSuccessful
        }catch (e: Exception) {

            Log.i("LOAD", "Error Occurred: ${e.message}")
        }
        return false
    }

    fun delListtoHash(titre: String) : Boolean{
        try{
            var id = ""
            for (k in DataProvider.getList()){
                if(k.GetTitre() == titre){
                    id= k.getID().toString()
                }
            }

            val client = OkHttpClient().newBuilder()
                .build()
            val mediaType = MediaType.parse("text/plain")
            val body = RequestBody.create(mediaType, "")
            val request: Request = Request.Builder()
                .url("http://tomnab.fr/todo-api/lists/$id")
                .method("DELETE", body)
                .addHeader("hash", DataProvider.getUser().HASH)
                .build()
            val response = client.newCall(request).execute()
            return response.isSuccessful
        }catch (e: Exception) {

            Log.i("LOAD", "Error Occurred: ${e.message}")
        }
        return false
    }

    fun getListwHash(){
        try{
        val client = OkHttpClient().newBuilder()
            .build()


        val request = Request.Builder()
            .url("http://tomnab.fr/todo-api/lists?hash=${DataProvider.getUser().HASH}")
            .method("GET", null)
            .build()
        val response = client.newCall(request).execute()
        //Log.i("LOAD", "Response Occurred: " + response.toString())


        val Rbody = response.body()?.string().toString()
        val RbodyJSON = JSONObject(Rbody)
        Log.i("LOAD", "JSON:  " + RbodyJSON.toString())
        val ListfJSON = RbodyJSON.getJSONArray("lists")
        //Log.i("LOAD",ListfJSON.toString())

        val ListeListe : ArrayList<ListeTodo> = ArrayList()
        for(k in 0..(ListfJSON.length()-1)){
            val nextL = ListfJSON.getJSONObject(k)
            val id = nextL.getString("id")
            val label = nextL.getString("label")
            //Log.i("LOAD","label : $label \t id : $id")
            ListeListe.add(ListeTodo(label, null, id))
        }
        DataProvider.setList(ListeListe)
        Log.i("LOAD", DataProvider.getList().toString())

        }
        catch (e: Exception) {

            Log.i("LOAD", "Error Occurred: ${e.message}")
        }

    }

    fun getItemswList(titre: String): Boolean {
        try {
            val Liste = DataProvider.getUser().getListToDo(titre)
            Log.i("LOAD", "id: " + Liste.getID().toString())
            DataProvider.getUser().idListCourante = Liste.getID().toString()
            val client = OkHttpClient().newBuilder()
                .build()
            val request: Request = Request.Builder()
                .url("http://tomnab.fr/todo-api/lists/${Liste.getID()}/items")
                .method("GET", null)
                .addHeader("hash", DataProvider.getUser().HASH)
                .build()
            val response = client.newCall(request).execute()


            val Rbody = response.body()?.string().toString()
            val RbodyJSON = JSONObject(Rbody)

            val ListfJSON = RbodyJSON.getJSONArray("items")

            val ListeItem: ArrayList<ItemToDo> = ArrayList()
            for (k in 0..(ListfJSON.length() - 1)) {
                //Log.i("LOAD", k.toString())
                val nextI = ListfJSON.getJSONObject(k)
                val label = nextI.getString("label")
                val stateString = nextI.getString("checked")
                val id = nextI.getString("id")
                var state =false
                if(stateString=="1"){
                    state = true
                }
                //Log.i("LOAD","label : $label \t state : ")
                Liste.AddItem(ItemToDo(label, state, id))

                }
            return true
            }   catch (e: Exception) {

        Log.i("LOAD", "Error Occurred: ${e.message}")
    }
    return false

    }

    fun addItemtoListtoHash(Titre: String, id: String?) : Boolean{

        try{
            val client = OkHttpClient().newBuilder()
                .build()
            val mediaType = MediaType.parse("text/plain")
            val body = RequestBody.create(mediaType, "")
            val request: Request = Request.Builder()
                .url("http://tomnab.fr/todo-api/lists/$id/items?label=$Titre")
                .method("POST", body)
                .addHeader("hash", DataProvider.getUser().HASH)
                .build()
            val response = client.newCall(request).execute()
            Log.i("LOAD", "body add item" + response.body()?.string().toString())
            return true
        }catch (e: Exception) {

            Log.i("LOAD", "Error Occurred: ${e.message}")
        }
        return false
    }

    fun delItemToListtoHash(TitreItem: String, TitreListe: String) : Boolean{
        try{
        val Liste = DataProvider.getUser().getListToDo(TitreListe)
        val Item = Liste.GetItemToDo(TitreItem)
            //Log.i("LOAD", "ID list :" + DataProvider.getUser().idListCourante+ "\n ID item:"+Item.getID())

        val client = OkHttpClient().newBuilder()
            .build()
        val mediaType = MediaType.parse("text/plain")
        val body = RequestBody.create(mediaType, "")
        val request: Request = Request.Builder()
            .url("http://tomnab.fr/todo-api/lists/${DataProvider.getUser().idListCourante}/items/${Item.getID()}")
            .method("DELETE", body)
            .addHeader("hash", DataProvider.getUser().HASH)
            .build()
        val response = client.newCall(request).execute()

        //Log.i("LOAD", "DEL :" + response.body()?.string().toString())
            return true
        } catch (e: Exception) {

            Log.i("LOAD", "Error Occurred: ${e.message}")
        }
        return false

    }

    fun LogInbase(){
        try{

            val pseudoInPref = sp.getString("login", "tom")
            val MdPInPref = sp.getString("MdP", "web")

            val client = OkHttpClient().newBuilder()
                .build()
            var mediaType = MediaType.parse("text/plain")
            var body = RequestBody.create(mediaType, "")

            var request = Request.Builder()
                .url("http://tomnab.fr/todo-api/authenticate?user=$pseudoInPref&password=$MdPInPref")
                .method("POST", body)
                //.addHeader("hash", "10bca641466d835d3db9be02ab6e1d08")
                .build()
            var response: Response = client.newCall(request).execute()
            //Log.i("LOAD", "Response Occurred: " + response.toString())


            val Rbody = response.body()?.string().toString()
            val RbodyJSON = JSONObject(Rbody)

            val HASHfJSON = RbodyJSON.getString("hash")
            DataProvider.setHash(HASHfJSON)
            Log.i("LOAD", DataProvider.getUser().toString())
        } catch (e: Exception) {

            Log.i("LOAD", "Error Occurred: ${e.message}")
        }

    }

    fun LogIn(pseudo: String, MdP: String){

        try{
            val client = OkHttpClient().newBuilder()
                .build()
            var mediaType = MediaType.parse("text/plain")
            var body = RequestBody.create(mediaType, "")

            var request = Request.Builder()
                .url("http://tomnab.fr/todo-api/authenticate?user=$pseudo&password=$MdP")
                .method("POST", body)
                //.addHeader("hash", "10bca641466d835d3db9be02ab6e1d08")
                .build()
            var response: Response = client.newCall(request).execute()
            //Log.i("LOAD", "Response Occurred: " + response.toString())


            val Rbody = response.body()?.string().toString()
            val RbodyJSON = JSONObject(Rbody)
            Log.i("LOAD", "JSON" + Rbody)
            val HASHfJSON = RbodyJSON.getString("hash")

            DataProvider.setHash(HASHfJSON)

            Log.i("LOAD", DataProvider.getUser().toString())


        } catch (e: Exception) {

            Log.i("LOAD", "Error Occurred: ${e.message}")
        }

    }

    fun TryLogIn(pseudo: String, MdP: String): Boolean {
        try{
        val client = OkHttpClient().newBuilder()
            .build()
        val mediaType = MediaType.parse("text/plain")
        val body = RequestBody.create(mediaType, "")
        val request: Request = Request.Builder()
            .url("http://tomnab.fr/todo-api/users?pseudo=$pseudo&pass=$MdP")
            .method("POST", body)
            .addHeader("hash", DataProvider.getUser().HASH)
            .build()
        val response = client.newCall(request).execute()

        val Rbody = response.body()?.string().toString()
            //Log.i("LOAD","avant if: "+ Rbody)
        if(Rbody =="<font color=\"red\">SQLInsert: Erreur de requete : Duplicate entry '$pseudo' for key 'pseudo'</font>"){
            Log.i("LOAD", "deja dedans: " + Rbody)
            LogIn(pseudo, MdP)
        }
            return true

    }
    catch (e: Exception) {

        Log.i("LOAD", "Error Occurred: ${e.message}")
    }
    return false
    }


    fun SwitchStateItem(TitreItem:String, TitreListe : String,check :String): Boolean {
        try{
            val Liste = DataProvider.getUser().getListToDo(TitreListe)
            val Item = Liste.GetItemToDo(TitreItem)

            val client = OkHttpClient().newBuilder()
                .build()
            val mediaType = MediaType.parse("text/plain")
            val body = RequestBody.create(mediaType, "")
            val request: Request = Request.Builder()
                .url("http://tomnab.fr/todo-api/lists/${DataProvider.getUser().idListCourante}/items/${Item.getID()}?check=${check}")
                .method("PUT", body)
                .addHeader("hash", DataProvider.getUser().HASH)
                .build()
            val response = client.newCall(request).execute()
            Log.i("LOAD",response.body()?.string().toString())


            return true
        } catch (e: Exception) {

            Log.i("LOAD", "Error Occurred: ${e.message}")
        }
        return false

    }


   /* protected override fun onPostExecute(result: String)  : Boolean{

        if(result == "ListAdded"){
            return true
        }
        return false
    }*/


    @RequiresApi(Build.VERSION_CODES.N)
    override fun doInBackground(vararg params: String): String {
        if(DataProvider.firstLog){
            LogInbase()
            DataProvider.firstLog = false
        }

        if(params[0] == LOG){
         //   val BOOL : Boolean =TryLogIn("tom","web")
        val BOOL : Boolean =TryLogIn(DataProvider.getPseudo(), DataProvider.getMdP())
            Log.i("LOAD", BOOL.toString())
        if(BOOL) {

            DataProvider.isLOG = true
            //Log.i("LOAD", "Pseudo: ${DataProvider.getPseudo()} \t ${DataProvider.getMdP()}")
            getListwHash()
            //Log.i("LOAD", "List: ${DataProvider.getList()}")
            //getItemswList(DataProvider.getList()[0].GetTitre())
        }
        }

        if(params[0] == AddList) {

            val add: Boolean = DataProvider.addList(params[1])

            if (add) {
                val BOOL: Boolean = addListtoHash(params[1])
                Log.i("LOAD", BOOL.toString())
                if (BOOL) {
                    //DataProvider.addList(params[1])
                    getListwHash()
                    return ("ListAdded")
                } else {
                    DataProvider.delList(params[1])
                    return ("not Done")
                }
            }
        }

        if(params[0] == DelList){
            //   val BOOL : Boolean =TryLogIn("tom","web")
            val BOOL : Boolean = delListtoHash(params[1])
            Log.i("LOAD", BOOL.toString())
            if(BOOL) {
                DataProvider.delList(params[1])

                return("ListDel")
            }
        }

        if(params[0] == AddItem) {
            val Liste = DataProvider.getUser().getListToDo(params[2])
            var bool = Liste.AddItem(ItemToDo(params[1], false))
            Log.i("LOAD", "avant if" + bool.toString())
            if (bool) {
                val BOOL: Boolean = addItemtoListtoHash(params[1],
                    DataProvider.getUser().idListCourante)
                Log.i("LOAD", BOOL.toString())
                if (BOOL) {

                    getItemswList(params[2])
                    return ("ItemAdded")
                } else {
                    Liste.RemoveItem(params[1])
                    //DataProvider.delList(params[1])
                    return ("not Done")
                }
            }
        }

        if(params[0] == DelItem){
                val Liste = DataProvider.getUser().getListToDo(params[2])


                val BOOL: Boolean = delItemToListtoHash(params[1], params[2])
                Log.i("LOAD", BOOL.toString())
                if (BOOL) {
                    Liste.RemoveItem(params[1])
                    //getItemswList(DataProvider.getUser().idListCourante)
                    return ("ItemDel")
                } else {

                    //DataProvider.delList(params[1])
                    return ("not Done")
                }


        }

        if(params[0] == GetItems){

            val BOOL =getItemswList(params[1])
            if(BOOL){
                return "ItemGeted"
            }
        }

        if(params[0] == SwitchState){
            val Liste = DataProvider.getUser().getListToDo(params[2])
            val Item = Liste.GetItemToDo(params[1])
            Item.SwitchState()
            var bool : Boolean = false
            if(Item.GetState()){
                bool = SwitchStateItem(params[1],params[2],"1")
            }else{
                bool = SwitchStateItem(params[1],params[2],"0")
            }
            if (bool) {

                getItemswList(params[2])
                return ("ItemStateSwitched")
            } else {
                //DataProvider.delList(params[1])
                return ("not Done")
            }


        }







        return "done"
    }


}