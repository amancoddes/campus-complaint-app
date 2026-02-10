//package com.example.hey
//
//import android.app.Application
//import android.content.Context
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.layout.wrapContentWidth
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.room.Dao
//import androidx.room.Database
//import androidx.room.Delete
//import androidx.room.Entity
//import androidx.room.Insert
//import androidx.room.PrimaryKey
//import androidx.room.Query
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import kotlinx.coroutines.InternalCoroutinesApi
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.internal.synchronized
//import kotlinx.coroutines.launch
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.room.migration.Migration
//import androidx.sqlite.db.SupportSQLiteDatabase
//
//@Entity(tableName = "student")
//data class StudentEntity(
//    @PrimaryKey(autoGenerate = true) val rollNo:Int=0,
//    val name:String,
//    val age:Int=17,
//    val hobby:String="nothing"
//)
//
//@Dao
//interface QueriesStudent{
//    @Insert
//    suspend fun insert(note:StudentEntity)
//
//    @Delete
//    suspend fun delete(note: StudentEntity)
//
//    @Query("SELECT * FROM student")
//    fun set():Flow<List<StudentEntity>>// fow ka use karo to suspend use nhi karna hai
//}
//
//
//// @Database bole ga ki room compiler es class ko implement karo or apna data room compiler ko de dega kyase tabenaksha
//// or dakho hum inerterface clasa bana kar bhi lakin hume RoomDatabase ko inherit bhi to karna tha
//// or hume quries ko bhi immplement karvana hai manuallly karne se acha hai compiler ko vo inter face bana kar de do
//// aak function banao uska return type use interface ko kar do room compiler us unimmplement class kko dakh lega or use bhi immplement kar dega
////room compiler sird upar ki thin line ko dakh kar hie class ko implement kar dega or Queries ka class alag immplementaition class bana dega lakin
////....StudentDatabase ka implemention class ko Queries class ka aak refernce dedega
////...matlab aak ka object bananna hai , queries ka liye bas -> obj.studentDaoImplementFunction() ka use karna hai queries ke class ka object ko return karne ke liye
//@Database(entities = [StudentEntity::class], version = 3, exportSchema = false)
//abstract class StudentDatabase:RoomDatabase(){
//    abstract fun studentDaoimmplementFunction():QueriesStudent
//
//
//    companion object {
//        // hume esme @Volatile ka use karnahoga agr aak se jada thread (fucniton se Instance lene aaye to ) , so you synchronized() or aak thread na obejct crete  kar kiye to dusre ko turent batane ka liye use @VVolatile variable
//        // kabhi kabhi merory cached hone ki wajah se thread ko abhi null hie dikh raha hoga
//        @Volatile
//        var instanceOfDataClassVariable:StudentDatabase?=null
//        @OptIn(InternalCoroutinesApi::class)
//
//        fun instanceOfDatabse(context22: Context): StudentDatabase {
//            return instanceOfDataClassVariable?: synchronized(this) {
//                val obj=Room.databaseBuilder(
//                    context = context22,
//                    klass = StudentDatabase::class.java,// path dega fiell kaha store hogi or applllication lifecycle se add kar dega matalab ja app ko uninstall karoga to ye data base bhi delete ho jayega
//                    name = "student_db"
//                ).addMigrations(object :Migration(1,2){// agar chota or simple migration hai to use anonymnous object
//                override fun migrate(db: SupportSQLiteDatabase) {
//                    db.execSQL("ALTER TABLE student ADD COLUMN age INTEGER NOT NULL DEFAULT 17 ")
//                }
//                }).addMigrations(object :Migration(2,3){
//                    override fun migrate(db: SupportSQLiteDatabase) {
//                        db.execSQL("ALTER TABLE student  ADD COLUMN hobby TEXT NOT  NULL DEFAULT 'nothing' ")
//                    }
//                }).build()// its give obj of implement class
//                instanceOfDataClassVariable=obj
//                obj
//            }
//
//        }
//    }
//}
//
//class StudentRepository(private val obj:QueriesStudent){
//    suspend fun insertFunction(student:StudentEntity){
//        obj.insert(note = student)
//    }
//
//    suspend fun deleteFunction(student: StudentEntity){
//        obj.delete(note = student)
//    }
//
//    fun setFunction():Flow<List<StudentEntity>>{
//        return obj.set()
//    }
//}
//
//class ViewModelStudent(val repository:StudentRepository):ViewModel() {
//    private val _studentList = MutableStateFlow<List<StudentEntity>>(emptyList())
//    val studentList = _studentList.asStateFlow()
//
//    init {
//        setFunctionUI()
//    }
//
//    private fun setFunctionUI() {// flow ko supend ki jarurt nhi padti lakin phir bhi aak tarah se supend jaysa hie use kar raha hi isliye use allso viewmodelscope, or viewmodel class ko inherit bhi karna hai , or collect{} esko linhna ahi pura kyu
//
//        // curcly braces wwla collect nhi milega
//        viewModelScope.launch {
//            repository.setFunction().collect { dataList ->
//                _studentList.value = dataList
//            }
//        }
//    }
//
//    fun insertFunctionUi(student:StudentEntity){
//        viewModelScope.launch {
//            repository.insertFunction(student=student)
//        }
//    }
//    fun deleteFunctionUi(student: StudentEntity){
//        viewModelScope.launch {
//            repository.deleteFunction(student=student)
//        }
//    }
//}
//class ClassFactory( val context1: Context):ViewModelProvider.Factory{
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        // if ka use karege ki konsa viewmodel ka liye condition run karna hai or agar na mile to alag code run kar do
//        if(modelClass.isAssignableFrom(ViewModelStudent::class.java)){
//            val querieImplementation=StudentDatabase.instanceOfDatabse(context22 = context1)
//            val repositoryInstanceStudent=StudentRepository(querieImplementation.studentDaoimmplementFunction())
//            return ViewModelStudent(repositoryInstanceStudent) as T
//        }
//
//        else{
//            throw Exception("viewModel class not present")
//        }
//
//    }
//}
//
//@Composable
//fun StudentListApp(){
//    val context1= LocalContext.current
//    val context= context1.applicationContext as Application
//
//    val view:ViewModelStudent= viewModel(factory = ClassFactory(context1 = context))
//    val studentListUi by view.studentList.collectAsState()
//
//    Column(modifier = Modifier.fillMaxSize().background(brush = Brush.sweepGradient(colors = listOf(
//        Color.Red, Color.White, Color.Black.copy(0.5f))))) {
//        val obj=StudentEntity(name = "soul")
//        Button(onClick = {view.insertFunctionUi(obj)}) { Text("add student") }
//
//        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)){items(studentListUi){
//                dataList->
//
//            Card(modifier = Modifier.width(200.dp).wrapContentHeight()) {
//                Text("roll no ${dataList.rollNo}")
//                HorizontalDivider(modifier = Modifier.wrapContentWidth(), thickness = 4.dp, color = Color.Blue.copy(0.4f))
//                Text("student name ${dataList.name}")
//                Text(dataList.hobby)
//                Button(onClick = {view.deleteFunctionUi(dataList)}) { Text("delete") }
//            }
//        }
//        }
//    }
//}