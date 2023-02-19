package com.example.wordsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordsapp.databinding.FragmentWordListBinding


class WordListFragment : Fragment() {

    companion object {
        const val LETTER = "letter"
        const val SEARCH_PREFIX = "https://www.google.com/search?q="
    }

    private var _binding: FragmentWordListBinding? = null

    private val binding: FragmentWordListBinding
        get() = _binding!!

    private lateinit var letterId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            letterId = it.getString(LETTER).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWordListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /*

        AQUI SE RECUPERABA EL VALOR DE EXTRAS DEL INTENT, PERO NO ES UNA BUENA PRACTICA

        val letterId: String = activity?.intent?.extras?.getString(LETTER).toString()

        */

        //RECYCLERVIEW

        val recyclerView: RecyclerView = binding.recyclerView

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = WordAdapter(letterId, requireContext() )

        recyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )

        /*

        Esto cambiaba el titulo de la AppBar de la activity

        activity?.title = getString(R.string.detail_prefix) + " " + letterId

        */

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}