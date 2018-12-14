package com.rogeriocarmo.gnss_mobilecalculator.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rogeriocarmo.gnss_mobilecalculator.Controller.SingletronController;
import com.rogeriocarmo.gnss_mobilecalculator.R;

import static com.rogeriocarmo.gnss_mobilecalculator.View.Activity_Main.definir_sidebar_ativa;


/**
 * A simple {@link Fragment} subclass.
 * Activities that Fragment_Import this fragment must implement the
 * {@link Fragment_SaveTXT.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Import#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Import extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String LOG_fileName;
    private String LOG_directory;
    private String RINEX_fileName;
    private String RINEX_directory;
    SimpleFileDialog FileOpenDialog_LOG;
    SimpleFileDialog FileOpenDialog_RINEX;
    SingletronController controller;

    Button btnOpenLOG;
    TextView txtOpenLOG;
    Button btnOpenRINEX;
    TextView txtOpenRINEX;
    Button btnExecutar;

    public Fragment_Import() {
        // Required empty public constructor
    }

    private void open_logger() {
        FileOpenDialog_LOG =  new SimpleFileDialog(getContext(), "FileOpen",
                new SimpleFileDialog.SimpleFileDialogListener() {
                    @Override
                    public void onChosenDir(String chosenDir) {
                        // The code in this function will be executed when the dialog OK button is pushed
//                        String m_chosen = chosenDir;
                        LOG_fileName = FileOpenDialog_LOG.getSelected_File_Name();
                        LOG_directory = FileOpenDialog_LOG.getSelected_File_Directory();

                        txtOpenLOG.setText(LOG_fileName);
                        txtOpenLOG.setTextColor(Color.GREEN);

                        btnOpenRINEX.setEnabled(true);
                    }
                });
        //You can change the default filename using the public variable "Default_File_Name"
        FileOpenDialog_LOG.Default_File_Name = "";
        FileOpenDialog_LOG.chooseFile_or_Dir();
    }

    private void open_RINEX() {
        FileOpenDialog_RINEX =  new SimpleFileDialog(getContext(), "FileOpen",
                new SimpleFileDialog.SimpleFileDialogListener() {
                    @Override
                    public void onChosenDir(String chosenDir) {
                        // The code in this function will be executed when the dialog OK button is pushed
//                        String m_chosen = chosenDir;
                        RINEX_fileName = FileOpenDialog_RINEX.getSelected_File_Name();
                        RINEX_directory = FileOpenDialog_RINEX.getSelected_File_Directory();

                        txtOpenRINEX.setText(RINEX_fileName);
                        txtOpenRINEX.setTextColor(Color.GREEN);

                        btnExecutar.setEnabled(true);
                    }
                });
        //You can change the default filename using the public variable "Default_File_Name"
        FileOpenDialog_RINEX.Default_File_Name = "";
        FileOpenDialog_RINEX.chooseFile_or_Dir();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_SaveTXT.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Import newInstance(String param1, String param2) {
        Fragment_Import fragment = new Fragment_Import();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_import, container, false);

        txtOpenLOG = view.findViewById(R.id.txtLogName);
        btnOpenLOG = view.findViewById(R.id.idBTN_OpenLOG);
        btnOpenLOG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_logger();
            }
        });

        txtOpenRINEX = view.findViewById(R.id.txtRINEXName);
        btnOpenRINEX = view.findViewById(R.id.idBTN_OpenRINEX);
        btnOpenRINEX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_RINEX();
            }
        });

        btnExecutar = view.findViewById(R.id.btnExecutar);

        btnExecutar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller = SingletronController.getInstance();

                controller.reiniciar_dados();

                controller.carregar_loger(LOG_fileName, LOG_directory);
                controller.carregar_RINEX(RINEX_fileName, RINEX_directory);

                // carregar rinex
                if (controller.isLogOpen() && controller.isRINEXOpen()){
                    Toast.makeText(getContext(), "Iniciando processamento...", Toast.LENGTH_SHORT).show(); //todo por 1 progress bar
                    controller.processamento_completo();
                    Toast.makeText(getContext(), "Processamento concluído!!!", Toast.LENGTH_LONG).show();
                    definir_sidebar_ativa();
                }

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        btnOpenRINEX.setEnabled(false);
        btnExecutar.setEnabled(false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
