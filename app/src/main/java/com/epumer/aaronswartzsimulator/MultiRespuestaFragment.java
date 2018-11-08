package com.epumer.aaronswartzsimulator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MultiRespuestaFragment.MultiRespuestaListener} interface
 * to handle interaction events.
 * Use the {@link MultiRespuestaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultiRespuestaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    protected Button back, next, pista;
    protected TextView tv;
    protected RadioGroup rg;

    private MultiRespuestaListener mListener;

    public MultiRespuestaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MultiRespuestaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MultiRespuestaFragment newInstance(String param1, String param2) {
        MultiRespuestaFragment fragment = new MultiRespuestaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_respuesta, container, false);
        tv = view.findViewById(R.id.pregunta_multi_text);
        rg = view.findViewById(R.id.radioMultiGroup);
        next = view.findViewById(R.id.nextMultiButton);
        back = view.findViewById(R.id.backMultiButton);
        pista = view.findViewById(R.id.pistaMultiButton);
        ponerPregunta(mListener.getPreguntaActual());

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);

                mListener.respuestaButtonListener(rb.getText());
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.nextButtonListener();
                ponerPregunta(mListener.getPreguntaActual());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.backButtonListener();
                ponerPregunta(mListener.getPreguntaActual());
            }
        });

        pista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.abrirPista();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void ponerPregunta(Pregunta pregunta) {
        tv.setText(pregunta.getId());
        int[] respuestas = ((PreguntaMultiRespuesta)pregunta).getRespuestas();
        RadioButton rb;
        rg.removeAllViews();
        for ( int respuesta : respuestas ) {
            rb = new RadioButton(getContext());
            rb.setText(respuesta);
            rg.addView(rb);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MultiRespuestaListener) {
            mListener = (MultiRespuestaListener) context;
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

    public interface MultiRespuestaListener {
        // TODO: Update argument type and name
        void respuestaButtonListener(Object respuesta);
        void nextButtonListener();
        void backButtonListener();
        void abrirPista();
        Pregunta getPreguntaActual();
    }
}
