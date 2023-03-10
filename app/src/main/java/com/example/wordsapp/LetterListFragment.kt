package com.example.wordsapp

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordsapp.data.SettingsDataStore
import com.example.wordsapp.databinding.FragmentLetterListBinding
import com.example.wordsapp.util.addMenuProvider
import com.example.wordsapp.util.getIconMenu
import kotlinx.coroutines.launch


class LetterListFragment : Fragment() {

    //VARIABLE DE VINCULACIÓN DE VISTA
    private var _binding: FragmentLetterListBinding? = null
    private val binding: FragmentLetterListBinding
        get() = _binding!!

    //ACCESO AL RECYCLERVIEW
    private lateinit var recyclerView: RecyclerView

    //BANDERA PARA SABER QUE TIPO DE LAYOUT TIENE RECYCLERVIEW,
    //
    private var isLinearLayoutManager = true

    private lateinit var settingsDataStore: SettingsDataStore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Se une/Infla el diseño XML
        _binding = FragmentLetterListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView

        //CREA EL MÉNU Y SETEA EL EVENTO CLICK DE LAS OPCIONES
        setUpMenu()

        //SE CREA LA INSTANCIA DEL DATASTORE
        settingsDataStore = SettingsDataStore( requireContext() )

        settingsDataStore.preferenceFlow.asLiveData().observe(
            viewLifecycleOwner
        ){ value ->
            isLinearLayoutManager = value
            //SE CAMBIA EL DISEÑO
            chooseLayoutManager()
            //REDIBUJAR EL ICONO SI ES NECESARIO
            setIcon( this.getIconMenu() )

        }



    }

    //MÉTODO NUEVO QUE PERMITE AL FRAGMENT CONTROLAR EL MENÚ DE OPCIONES
    //CON BASE EN UNA FUNCION DE EXTENSION CREADA EN EL ARCHIVO DE UTILIDADES
    private fun setUpMenu(){

        this.addMenuProvider(
            R.menu.layout_menu
        ){
            when(it.itemId){
                //CUANDO LE DEN CLICK AL ELEMENTO
                R.id.action_switch_layout -> {
                    //SE ACTUALIZA EL ICONO
                    setIcon( getIconMenu() )
                    //SE ACTUALIZA EL VALOR GUARDADO EN DATASTORE
                    lifecycleScope.launch {
                        settingsDataStore.saveLayoutToPreferencesStore(
                            !isLinearLayoutManager,
                            requireContext()
                        )
                    }
                    //RETORNAMOS EVENTO CAPTADO EXITOSAMENTE
                    true
                }
                else -> false
            }
        }

    }

    //MÉTODO PARA CAMBIAR LA ORIENTACIÓN DEL RECYCLER VIEW
    //CON BASE EN LA BANDERA
    private fun chooseLayoutManager() {

        if (isLinearLayoutManager) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            recyclerView.layoutManager = GridLayoutManager(context, 4)
        }

        recyclerView.adapter = LetterAdapter()

    }

    //MÉTODO PARA CAMBIAR EL ICONO DEL MENÚ
    private fun setIcon(menu: Menu?) {

        if (menu == null) return

        val menuItem: MenuItem = menu.findItem(R.id.action_switch_layout)

        menuItem.apply {
            icon = if (isLinearLayoutManager)
                ContextCompat.getDrawable( requireContext(), R.drawable.ic_grid_layout)
            else
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_linear_layout)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}