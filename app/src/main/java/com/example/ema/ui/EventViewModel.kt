import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ema.data.AppDatabase
import com.example.ema.data.dao.EventDao
import com.example.ema.data.entities.Event
import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val eventDao: EventDao = AppDatabase.getDatabase(application).eventDao()
    val events: LiveData<List<Event>> = eventDao.getAllEvents()

    fun fetchEvents() {
        // Fetch all events from the database
        viewModelScope.launch {
            // You can perform additional operations if needed
        }
    }

//    fun deleteEvent(event: Event) {
//        viewModelScope.launch {
//            eventDao.delete(event)
//        }
//    }
}
