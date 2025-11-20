package com.edunova.ui.screens.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.edunova.data.auth.AuthManager
import com.edunova.data.local.entity.AssessmentEntity
import com.edunova.databinding.FragmentQuizBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class QuizFragment : Fragment() {

	private var _binding: FragmentQuizBinding? = null
	private val binding get() = _binding!!

	private var selectedSubject: String = "Math"

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentQuizBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// If a subject was passed from Home, prefer it
		arguments?.getString("selectedSubject")?.let {
			selectedSubject = it
			binding.textSelectedSubject.text = it
		}

		binding.btnSubjectMath.setOnClickListener { selectSubject("Math") }
		binding.btnSubjectScience.setOnClickListener { selectSubject("Science") }
		binding.btnSubjectEnglish.setOnClickListener { selectSubject("English") }

		binding.btnStartQuiz.setOnClickListener {
			startQuiz()
		}
	}

	private fun selectSubject(subject: String) {
		selectedSubject = subject
		binding.textSelectedSubject.text = subject
	}

	private fun startQuiz() {
		val context = requireContext()
		val database = com.edunova.di.DatabaseModule.getDatabase(context)
		val lessonDao = database.lessonDao()
		val assessmentDao = database.assessmentDao()
		val auth = AuthManager(context)
		val studentId = auth.getCurrentStudentId() ?: return

		// Render simple in-page questions (5 MCQs)
		val questions = sampleQuestionsFor(selectedSubject)
		binding.questionsContainer.removeAllViews()
		val selections = mutableMapOf<Int, Int>() // qIndex -> optionIndex
		questions.forEachIndexed { index, q ->
			val qLayout = android.widget.LinearLayout(requireContext()).apply {
				orientation = android.widget.LinearLayout.VERTICAL
				setPadding(0, 8, 0, 8)
			}
			val title = android.widget.TextView(requireContext()).apply {
				text = "Q${index + 1}. ${q.question}"
				textSize = 16f
				setTextColor(android.graphics.Color.BLACK)
			}
			qLayout.addView(title)
			val group = android.widget.RadioGroup(requireContext())
			q.options.forEachIndexed { optIndex, opt ->
				val rb = android.widget.RadioButton(requireContext()).apply { text = opt }
				group.addView(rb)
			}
			group.setOnCheckedChangeListener { g, _ ->
				val selected = g.indexOfChild(g.findViewById(g.checkedRadioButtonId))
				if (selected >= 0) selections[index] = selected
			}
			qLayout.addView(group)
			binding.questionsContainer.addView(qLayout)
		}
		binding.btnSubmitQuiz.visibility = View.VISIBLE
		binding.btnSubmitQuiz.setOnClickListener {
			// Compute score and save
			val total = questions.size
			val correct = questions.indices.count { idx -> selections[idx] == questions[idx].correctIndex }
			val score = (correct.toFloat() / total) * 100f
			viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
				val lessons = lessonDao.getAllNow()
				val lessonId = lessons.firstOrNull { it.subject == selectedSubject }?.id ?: "lesson_${selectedSubject}"
				assessmentDao.insertAssessment(
					AssessmentEntity(
						id = UUID.randomUUID().toString(),
						studentId = studentId,
						lessonId = lessonId,
						score = score,
						totalQuestions = total,
						correctAnswers = correct,
						assessmentType = "QUIZ",
						questions = "[]",
						answers = "[]",
						timeTaken = 0
					)
				)
				launch(Dispatchers.Main) {
					Toast.makeText(requireContext(), "Score: ${score.toInt()}%", Toast.LENGTH_SHORT).show()
				}
			}
		}
	}

	private data class MCQ(val question: String, val options: List<String>, val correctIndex: Int)

	private fun sampleQuestionsFor(subject: String): List<MCQ> = when (subject) {
		"Math" -> listOf(
			MCQ("2 + 3 = ?", listOf("4", "5", "6", "7"), 1),
			MCQ("10 - 7 = ?", listOf("1", "2", "3", "4"), 2),
			MCQ("5 × 2 = ?", listOf("7", "8", "9", "10"), 3),
			MCQ("6 ÷ 3 = ?", listOf("1", "2", "3", "4"), 1),
			MCQ("Square of 4?", listOf("8", "12", "16", "20"), 2)
		)
		"Science" -> listOf(
			MCQ("H2O is?", listOf("Hydrogen", "Oxygen", "Water", "Salt"), 2),
			MCQ("Earth is a?", listOf("Star", "Planet", "Comet", "Asteroid"), 1),
			MCQ("Sun rises in the?", listOf("North", "South", "East", "West"), 2),
			MCQ("We breathe?", listOf("CO2", "O2", "N2", "H2"), 1),
			MCQ("Plants make food by?", listOf("Respiration", "Photosynthesis", "Digestion", "Transpiration"), 1)
		)
		else -> listOf(
			MCQ("Choose noun:", listOf("Quickly", "Beautiful", "Cat", "Run"), 2),
			MCQ("Past of go:", listOf("Gone", "Went", "Going", "Go"), 1),
			MCQ("Opposite of hot:", listOf("Cold", "Warm", "Boil", "Heat"), 0),
			MCQ("Plural of child:", listOf("Childs", "Childes", "Children", "Childrens"), 2),
			MCQ("Article before apple:", listOf("A", "An", "The", "No"), 1)
		)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}

