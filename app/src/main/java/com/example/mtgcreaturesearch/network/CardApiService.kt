import com.example.mtgcreaturesearch.Model.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
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
//    @GET("search?pretty=true&order=name&q=%28game%3Apaper%29+cmc>%3D15")
    @GET("search?q=type%3Acreature+game%3Apaper+cmc>%3D10&unique=cards&as=grid&order=name")

    // All creatures. like 15000 cards~
//    @GET("search?q=type%3Acreature+game%3Apaper&unique=cards&as=grid&order=name")
    suspend fun getPhotos(): Card


}
object CardApi{
    val retrofitService : CardApiService by lazy {
        retrofit.create(CardApiService::class.java)
    }
}