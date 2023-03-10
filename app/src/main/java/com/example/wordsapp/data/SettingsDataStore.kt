package com.example.wordsapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

//NOMBRE DEL ALMACEN O BASE DE DATOS COMO ROOM
private const val LAYOUT_PREFERENCES_NAME = "layout_preferences"

//MÉTODO DE EXTENSIÓN A CONTEXT, ACTUA COMO UN SINGLETON Y ESTA
//DISPONIBLE PARA TODA LA APP, ACÁ SE CREA EL ALMACEN(ARCHIVO)
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCES_NAME
)

class SettingsDataStore(private val context: Context) {

    //SE CREA LA LLAVE PARA EL VALOR BOOLEAN QUE SE VA ALMACENAR
    private val IS_LINEAR_LAYOUT_MANAGER = booleanPreferencesKey("is_linear_layout_manager")

    suspend fun saveLayoutToPreferencesStore(
        isLinearLayoutManager: Boolean,
        context: Context
    ){
        //OBTENEMOS EL ALMACEN A PARTIR DE EDIT Y COMO ESTE RETORNA EL
        //OBJETO DE PREFERENCIAS QUE SERIA ALGO ASI COMO UN MAPA
        //ALMACENAMOS EN ESTE LA LLAVE Y EL VALOR
        context.dataStore.edit { preferences ->
            preferences[IS_LINEAR_LAYOUT_MANAGER] = isLinearLayoutManager
        }
    }

    //EN ESTE CASO USAMOS MAP PARA ESTRAER DEL OBJETO PREFERENCES, Y APARTIR  DE ESTE,
    //EL VALOR DE LA LLAVE QUE GUARDAMOS, COMO ESTA VARIABLE VA A ALMACENAR UN FLOW
    //DESDE OTROS ARCHIVOS LA PODEMOS COLLECTAR
    val preferenceFlow: Flow<Boolean> = context.dataStore.data
        .catch {
            //SI SUCEDE ALGUNA EXCEPCION DE IO, EMITA UN PREFERENCES VACIO
            //DE LO CONTRARIO SI ES OTRO TIPO DE ERROR, ARROJE EL ERROR
            if( it is IOException){
                it.printStackTrace()
                emit( emptyPreferences() )
            }else throw it
        }.map { preferences ->
            preferences[IS_LINEAR_LAYOUT_MANAGER] ?: true
        }


}