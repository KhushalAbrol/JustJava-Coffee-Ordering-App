package com.example.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    int basePrice = 5;
    int toppingPrice = 0;
    Toast toastMessage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText nameEditText = findViewById(R.id.name_edit_text);
        String name = nameEditText.getText().toString();
        if(name.equalsIgnoreCase("")) {
            nameEditText.setError(getString(R.string.name_required));
            viewToast(getString(R.string.enter_your_name));
        }else {
            CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
            boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
            CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
            boolean hasChocolate = chocolateCheckBox.isChecked();
            display(quantity);
            String summary = createOrderSummary(name, hasWhippedCream, hasChocolate);
            Intent email = new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.parse("mailto:"));
            email.putExtra(Intent.EXTRA_SUBJECT, "Order for "+quantity+" Cups Of Coffee!");
            email.putExtra(Intent.EXTRA_TEXT, summary);
            if(email.resolveActivity(getPackageManager()) !=  null){
                startActivity(email);
            }
            displayOrderSummery(summary);
            viewToast("Order for "+quantity+" cup of coffees for $"+ (basePrice+toppingPrice)*quantity);
        }
    }

    /**
     * viewToast make a toast with the price and quantity on the item
     * */
    void viewToast(String message) {
        if (toastMessage != null) {
            toastMessage.cancel();
        }
        toastMessage = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toastMessage.show();
    }

    /**
     * Create the Order Summary
     * @param name of the customer
     * @param hasWhippedCream added or not
     * @param hasChocolate added or not
     * @return the message of order summary
     */
    private String createOrderSummary(String name, boolean hasWhippedCream, boolean hasChocolate){
        return getString(R.string.name_text) + name + "\n"+ getString(R.string.quantity_text)+ quantity + "\n"+ getString(R.string.add_whipped_cream)+ hasWhippedCream + "\n"+ getString(R.string.add_chocolate)+ hasChocolate +"\n"+ getString(R.string.total_amount)+calculatePrice( hasWhippedCream, hasChocolate) +"\n"+ getString(R.string.thank_you);
    }

    /**
     * Calculate the Total Price of the Order
     * @param hasWhippedCream added or not
     * @param hasChocolate  added or not
     * @return the total price of order
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate){
        return quantity*(basePrice+calculateToppingPrice(hasWhippedCream, hasChocolate));
    }

    /**
     * calculateToppingPrice calculate additional price per cup for toppings
     * @param hasWhippedCream added or not
     * @param hasChocolate or not
     * @return the topping price per cup
     * */
    private int calculateToppingPrice(boolean hasWhippedCream, boolean hasChocolate){
        toppingPrice = 0;
        if(hasWhippedCream) toppingPrice++;
        if(hasChocolate) toppingPrice+=2;
        return toppingPrice;
    }

    /**
     * This method displays the given quantity value on the screen.
     * @param quantity is the number of item ordered
     */
    private void display(int quantity) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" +quantity);
    }

    /**
     * This method displays the given order summary on the screen.
     * @param summary of the order to be printed on the screen
     */
    private void displayOrderSummery(String summary) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(summary);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if(quantity<10){
            quantity++;
            display(quantity);
        }else{
            viewToast(getString(R.string.maximum_coffee));
        }
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if(quantity>1){
            quantity--;
            display(quantity);
        }else{
            viewToast(getString(R.string.minimum_coffee));
        }
    }
}