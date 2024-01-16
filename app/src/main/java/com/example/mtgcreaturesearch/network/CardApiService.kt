import com.example.mtgcreaturesearch.Model.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

val json = Json { isLenient = true; prettyPrint = true; ignoreUnknownKeys = true; }

private const val BASE_URL =
    "https://api.scryfall.com/cards/"
//private val json = Json { ignoreUnknownKeys = true }
private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
//    .addConverterFactory(GsonConverterFactory.create())

    .baseUrl(BASE_URL)
    .build()


interface CardApiService {
    //Works
   @GET("search")
    //works now but 1 page is 175 cards.
    // 174 cards. works??? Mv = 9
    //@GET("search?pretty=true&order=name&q=%28game%3Apaper%29+cmc>%3D9&pretty=true")

    // All creatures. like 15000 cards~
     //@GET("search?q=type%3Acreature+game%3Apaper&unique=cards&as=grid&order=name")
     // Page 2
    // @GET("search?format=json&include_extras=false&include_multilingual=false&include_variations=false&order=name&page=2&q=type%3Acreature+game%3Apaper&unique=cards")

    //Page of 175 cards. Includes transform cards.
    //@GET("search?format=json&include_extras=false&include_multilingual=false&include_variations=false&order=name&q=%28game%3Apaper%29+c%3Ared+pow%3D3+%28game%3Apaper%29&unique=cards")

    //739 CARDS
    //@GET("search?format=json&order=cmc&q=c%3Ared+pow%3D3&pretty=true")

    //Single transform card
    //@GET("search?q=Tormented+Pariah&unique=cards&as=grid&order=name")

    // All
    suspend fun getPhotos(@Query("order", encoded = true) order: String = "name", @Query("q", encoded = true) q: String = "type%3Acreature+%28game%3Apaper%29", @Query("page", encoded = false) page: String): Card


}
object CardApi{
    val retrofitService : CardApiService by lazy {
        retrofit.create(CardApiService::class.java)
    }
}