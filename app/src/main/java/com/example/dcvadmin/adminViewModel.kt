package com.example.dcvadmin

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AdminViewModel@Inject constructor(
    private val db: FirebaseFirestore,
) : ViewModel(){
init{
    populatePendingNode()
}
    fun populatePendingNode() = CoroutineScope(Dispatchers.Main).launch {
        db.collection("nodes")
            .addSnapshotListener { value, error ->
                if (error != null) {

                }
                if (value != null) {
                    savedNodes.value = value.documents.mapNotNull {
                        it.toObject<Node>()
                    }

                }
            }

    }
    fun approve(node : Node)= CoroutineScope(Dispatchers.IO).launch{
        db.collection("approvedNode")
            .document(node.auth)
            .set(node)
            .await()

         db.collection("nodes")
            .document(node.auth)
            .delete()
             .await()
//            .whereEqualTo("auth",node.auth)
//            .get()
//            .await()
//        if (del.documents.isNotEmpty()) {
//            for (document in del) {
//                try {
//                    document.reference.delete().await()
//                } catch (e: FirebaseFirestoreException) {
//
//                } catch (e: Exception) {
//
//                }
//            }
//        }
    }

}