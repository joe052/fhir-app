/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.fhir.codelabs.engine

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.google.android.fhir.datacapture.QuestionnaireFragment
import com.google.android.fhir.datacapture.mapping.ResourceMapper
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Questionnaire

class MainActivity : AppCompatActivity() {
  var questionnaireJsonString: String? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    //setContentView(R.layout.patientform)

    //Add a questionnaire fragment.
    questionnaireJsonString = getStringFromAssets("patient_questionnaire.json")

//    if (savedInstanceState == null) {
//      supportFragmentManager.commit {
//        setReorderingAllowed(true)
//        add(
//          R.id.fragment_container_view,
//          QuestionnaireFragment.builder().setQuestionnaire(questionnaireJsonString!!).build()
//        )
//      }
//    }
  }
  private fun submitQuestionnaire() {

    // 5 Get a questionnaire response.
    // Get a questionnaire response
    val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view)
            as QuestionnaireFragment
    val questionnaireResponse = fragment.getQuestionnaireResponse()

    // Print the response to the log
    val jsonParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
    val questionnaireResponseString =
      jsonParser.encodeResourceToString(questionnaireResponse)
    Log.d("response", questionnaireResponseString)

    // 6 Extract FHIR resources from QuestionnaireResponse.
    lifecycleScope.launch {
      val questionnaire =
        jsonParser.parseResource(questionnaireJsonString) as Questionnaire
      val bundle = ResourceMapper.extract(questionnaire, questionnaireResponse)
      Log.d("extraction result", jsonParser.encodeResourceToString(bundle))
    }

  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.submit_patient_form, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.submit) {
      submitQuestionnaire()
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  private fun getStringFromAssets(fileName: String): String {
    return assets.open(fileName).bufferedReader().use { it.readText() }
  }
}
