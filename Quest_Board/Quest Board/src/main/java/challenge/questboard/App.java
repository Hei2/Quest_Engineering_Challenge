package challenge.questboard;

import android.app.Application;
import com.parse.Parse;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();

        // Create the connection to the Parse database
        Parse.initialize(this, "L0mf97AsKGY4oQ1QHEf77QzE6nvUIvA0C3LZ7GiZ", "zI7Bk54SIJl786oNjR0RMMkv8d8SL41GbrnLb8L9");
    }
}