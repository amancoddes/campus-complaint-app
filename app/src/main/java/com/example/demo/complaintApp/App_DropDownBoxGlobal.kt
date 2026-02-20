package com.example.demo.complaintApp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


// BOX THERE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleDropdownSelector(modifier: Modifier = Modifier, complainTitle: String, listTitles:List<String>, onTitleSelected: (String) -> Unit, boxShow:String) {
    // Short, professional list of complaint titles
    val titles = listTitles

    var expanded by remember { mutableStateOf(false) }

    // ExposedDropdownMenuBox gives a compact anchored popup (dropdown)
    // this fucntion observe the Anchor and run the onExpandedChange

    ExposedDropdownMenuBox(
        expanded = expanded, // true matt karna kyu phir back tap karne par phir se drop down open ho jayega
        onExpandedChange = { expanded = !expanded },//// ExposedDropdownMenu ye esko run karta hai anchor ko observe karne ke baad
        modifier = modifier
    ) {
        // Read-only OutlinedTextField styled like a button
        OutlinedTextField(
            value = complainTitle,
            onValueChange = { /* read-only */ },
            readOnly = true,
            label = { Text(text = boxShow) },
            modifier = Modifier
                // new API: pass MenuAnchorType (PrimaryNotEditable) so popup anchors correctly
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)// its rwegister the anchor , and this anchor observe by the ExposedDropdownMenuBox and now its run  onExpandedChange
                ,
            trailingIcon = { TrailingIcon(expanded = expanded) },// yaha par Trailling method bankar defien nhi kiya ,   ExposedDropdownMenuBox ne Trailling ko likha hai apne liye
            singleLine = true
        )


        // The small anchored popup box (scrollable when many items)
        ExposedDropdownMenu(
            expanded = expanded,// its just open the box but DropdownMenuItem add the items in the box
            onDismissRequest = { expanded = false },// ye tab kaam mei aata hai jab user drop down ke open ke baad kuch choose nhi karna chahte or bas baki screen par tap ke ke close karna chahta ho box ko
            modifier = Modifier
                .fillMaxWidth(0.98f) // slight inset so popup doesn't feel glued to edges
        ) {




            // Use a limited height so the popup becomes scrollable for longer lists
            Column(modifier = Modifier.heightIn(max = 240.dp) .verticalScroll(rememberScrollState())) {// coloumn ke bina bhi show ho jaye lekin size fixed bhi to karna hai
                titles.forEach { item ->
                    DropdownMenuItem(

                        text = {


                            Text(
                                text = item,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,// agar jada lambas string jo mobile ki width se jada to -> ..... ye use karega
                                fontWeight = if (item == complainTitle) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            onTitleSelected(item)
                            expanded = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}