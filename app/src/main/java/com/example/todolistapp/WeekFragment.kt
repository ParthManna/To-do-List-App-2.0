import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.CalenderAdapter
import com.example.todolistapp.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeekFragment : Fragment() {

    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var weekText: TextView
    private lateinit var selectedDate: LocalDate

    companion object {
        private const val ARG_DATE = "date"

        @RequiresApi(Build.VERSION_CODES.O)
        fun newInstance(date: LocalDate): WeekFragment {
            val fragment = WeekFragment()
            val args = Bundle()
            args.putString(ARG_DATE, date.toString())
            fragment.arguments = args
            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedDate = LocalDate.parse(arguments?.getString(ARG_DATE))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_week, container, false)

        calendarRecyclerView = view.findViewById(R.id.recyclerViewWeek)
        weekText = view.findViewById(R.id.monthyeartv)

        setWeekView()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setWeekView() {
        val formattedDate = weekFromDate(selectedDate)
        weekText.text = formattedDate

        val daysInWeek: ArrayList<String> = daysInWeekArray(selectedDate)

        val calendarAdapter = CalendarAdapter2(daysInWeek, selectedDate, object : CalendarAdapter2.OnItemListener {
            override fun OnItemClick(position: Int, dayText: String?) {
                if (dayText.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Invalid date selected", Toast.LENGTH_SHORT).show()
                } else {
                    val message = "Selected Date: $dayText ${weekFromDate(selectedDate)}"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7) // 7 columns for 7 days
        calendarRecyclerView.adapter = calendarAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysInWeekArray(date: LocalDate): ArrayList<String> {
        val daysInWeekArray = ArrayList<String>()
        val currentDayOfWeek = date.dayOfWeek.value // Monday = 1, Sunday = 7

        // Get the start of the week (Monday)
        val startOfWeek = date.minusDays((currentDayOfWeek).toLong())

        // Add all 7 days of the week to the array
        for (i in 0 until 7) {
            daysInWeekArray.add(startOfWeek.plusDays(i.toLong()).dayOfMonth.toString())
        }

        return daysInWeekArray
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun weekFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter).uppercase()
    }
}
