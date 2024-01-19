import com.example.mtgcreaturesearch.Model.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

val json = Json { isLenient = true; prettyPrint = true; ignoreUnknownKeys = true; }

private const val BASE_URL =
    "https://api.scryfall.com/cards/"

/**
 * Builds a retrofit object using the kotlinx serialization converter.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

/**
 * Creates a retrofit service object with 3 different query parameters.
 * The order of the search result, which is defaulted to name.
 * The base query which searches for only creatures that have been printed on paper.
 * The page of the search we are querying.
 */
interface CardApiService {
    @GET("search")
    suspend fun getPhotos(
        @Query("order", encoded = true) order: String = "name",
        @Query("q", encoded = true) q: String = "type%3Acreature+%28game%3Apaper%29",
        @Query("page", encoded = false) page: String = "1"
    ): Card
}

/**
 * Creates a public Api object that exposes a lazy Retrofit service.
 */
object CardApi {
    val retrofitService: CardApiService by lazy {
        retrofit.create(CardApiService::class.java)
    }
}