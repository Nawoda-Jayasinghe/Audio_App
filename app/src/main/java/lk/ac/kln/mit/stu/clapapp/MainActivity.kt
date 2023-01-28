package lk.ac.kln.mit.stu.clapapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.service.controls.actions.FloatAction
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    //for media player api
    // ? safe call operator. help to avoid null exception
    private var mediaPlayer : MediaPlayer? = null
    private lateinit var seekBar : SeekBar
    private lateinit var runnable : Runnable

    private lateinit var handler:Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val played = findViewById<TextView>(R.id.tvPlayed)
        val due = findViewById<TextView>(R.id.tvDue)

        val play = findViewById<FloatingActionButton>(R.id.fabPlay)
        val pause = findViewById<FloatingActionButton>(R.id.fabPause)
        val stop = findViewById<FloatingActionButton>(R.id.fabStop)

        seekBar = findViewById(R.id.sbClapping)
    handler = Handler(Looper.getMainLooper())

        play.setOnClickListener{
            if(mediaPlayer==null){
                mediaPlayer = MediaPlayer.create(this, R.raw.music)
                initializeSeekBar()
            }
            mediaPlayer?.start()
        }

        pause.setOnClickListener{
            mediaPlayer?.pause()
        }

        stop.setOnClickListener{
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
            seekBar.progress = 0
        }

//        val button = findViewById<Button>(R.id.btnClap)
//        button.setOnClickListener{
//            mediaPlayer.start()
//        }


    }

    //protected - all components inside the class and all the subclasses can access
    //internal - all the components inside the module can access
    private fun initializeSeekBar(){
    seekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if(fromUser)mediaPlayer?.seekTo(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }

    })

        val tvPlayed = findViewById<TextView>(R.id.tvPlayed)
        val tvDue = findViewById<TextView>(R.id.tvDue)
        // !!not null asertion operaator
        seekBar.max = mediaPlayer!!.duration

        runnable = Runnable{
            seekBar.progress = mediaPlayer!!.currentPosition

            val playedTime = mediaPlayer!!.currentPosition/1000
            tvPlayed.text = "$playedTime sec"
            val duration = mediaPlayer!!.duration/1000
            val dueTime = duration-playedTime
            tvDue.text = "$dueTime sec"
            //have to invoke post and delayed

            handler.postDelayed(runnable,1000) //thread will run after 1 sec delay
        }
        handler.postDelayed(runnable,1000)
    }
}