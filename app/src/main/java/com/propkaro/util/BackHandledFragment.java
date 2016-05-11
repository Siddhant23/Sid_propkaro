package com.propkaro.util;

import android.app.Fragment;
import android.os.Bundle;

//BASIC IMPLEMENTATION OF BACK HANDLED FRAGMENT

public abstract class BackHandledFragment extends Fragment {
 protected BackHandlerInterface backHandlerInterface;    
 public abstract String getTagText();
 public abstract boolean onBackPressed();

 @Override
 public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
	if(!(getActivity()  instanceof BackHandlerInterface)) {
	    throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
	} else {
	    backHandlerInterface = (BackHandlerInterface) getActivity();
	}
 }
	
 @Override
 public void onStart() {
     super.onStart();
		
	// Mark this fragment as the selected Fragment.
	backHandlerInterface.setSelectedFragment(this);
 }
	
 public interface BackHandlerInterface {
	public void setSelectedFragment(BackHandledFragment backHandledFragment);
 }
}   
//EXAMPLE BACK_STACK_BELOW
//A SAMPLE CLUTTERED onBackPressed() CALLBACK IN AN ACTIVITY
//WHICH MAINTAINS A REFERENCE TO THE CURRENTLY ACTIVE FRAGMENT.
//@Override
//public void onBackPressed() {
// if(selectedFragment.equals(fragmentA) && fragmentA.hasExpandedRow()) {
//     fragmentA.collapseRow();
// } else if(selectedFragment.equals(fragmentA) && fragmentA.isShowingLoginView()) {
//     fragmentA.hideLoginView();
// } else if(selectedFragment.equals(fragmentA)) {
//     popBackStack();
// } else if(selectedFragment.equals(fragmentB) && fragmentB.hasCondition1()) {
//     fragmentB.reverseCondition1();
// } else if(selectedFragment.equals(fragmentB) && fragmentB.hasCondition2()) {
//     fragmentB.reverseCondition2();
// } else if(selectedFragment.equals(fragmentB)) {
//     popBackStack();
// } else {
//     // handle by activity
//     super.onBackPressed();
// }
//}
//-----------------------------------------------------
//BASIC ACTIVITY CODE THAT LETS ITS FRAGMENT UTILIZE onBackPress EVENTS 
//IN AN ADAPTIVE AND ORGANIZED PATTERN USING BackHandledFragment

//public class TheActivity extends FragmentActivity implements BackHandlerInterface {
// private BackHandledFragment selectedFragment;
//
// @Override
// public void onBackPressed() {
//     if(selectedFragment == null || !selectedFragment.onBackPressed()) {
//         // Selected fragment did not consume the back press event.
//         super.onBackPressed();
//     }
// }
//
// @Override
// public void setSelectedFragment(BackHandledFragment selectedFragment) {
//     this.selectedFragment = selectedFragment;
// }
//}
//---------------------------------------------------
//public class MyActivity {
//public void newFragment(String fragmentTag) {
//MyFragment myFragment = MyFragment.newInstance(fragmentTag);
//FragmentManager.beginTransaction()
//// give the BackStackEntry the same name as the fragment
//.add(myFragment, fragmentTag)
//.addToBackStack(fragmentTag)
//.commit;
//}
//
//@Override
//public void onBackPressed() {
////get the name from the topmost BackStackEntry which is also the fragment tag.
//String fragmentTag =
//FragmentManager.getBackStackEntryAt(FragmentManager.getBackStackCount()-1)
//.getName();
//
//MyFragment topmostFragment = FragmentManager.findFragmentByTag(fragmentTag);
//if (topmostFragment == null || !(topmostFragment instanceof BackHandledFragment) || !((BackHandledFragment) topmostFragment.onBackPressed())) {
//super.onBackPressed();
//}
//FragmentManager.popBackStack() //always pop?
//}
//}
//-----------------------------------------------------
