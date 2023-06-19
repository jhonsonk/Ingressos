package com.digitalingressos.ingressos.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.data.database.caixa.Caixa;
import com.digitalingressos.ingressos.data.database.caixa.CaixaRepository;
import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.digitalingressos.ingressos.data.database.AppDatabase;
import com.digitalingressos.ingressos.data.database.auth.*;
import com.digitalingressos.ingressos.data.database.pdvEmpresa.*;
import com.digitalingressos.ingressos.util.CustomDialog;
import com.digitalingressos.ingressos.util.NavigationHost;
import com.digitalingressos.ingressos.util.UtilKeyboard;
import com.digitalingressos.ingressos.util.Utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class LoginFragment extends Fragment {
    TextView mTvErro;
    EditText mEtSenha;
    EditText mEtLogin;
    LinearLayout mLayPdv;
    LinearLayout mLayUsuario;
    LinearLayout mLaySenha;
    Button mBtNext;
    Button mBtStep;
    Button mBtVoltar;
    Spinner mSpPdvEmpresa;
    String mLogin;
    String mSenha;
    PdvEmpresa mPdvSelecionado;
    private ProgressDialog mProgressDialog;
    private CustomDialog mDialogCustom;
    private String mTitleDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mTitleDialog = "Atenção";

        mDialogCustom = new CustomDialog(this.getContext());

        mProgressDialog = new ProgressDialog(this.getContext());
        mProgressDialog.setMessage("Processando...");
        mProgressDialog.show();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mTvErro = view.findViewById(R.id.tv_erro);
        mEtSenha = view.findViewById(R.id.et_senha);
        mEtLogin = view.findViewById(R.id.et_usuario);


        mEtLogin.setEnabled(true);
        mEtSenha.setEnabled(true);

        mBtNext = view.findViewById(R.id.bt_entrar);
        mBtStep = view.findViewById(R.id.bt_login_step);
        mBtVoltar = view.findViewById(R.id.bt_voltar);

        mSpPdvEmpresa = (Spinner) view.findViewById(R.id.sp_pdv);
        mLayPdv = view.findViewById(R.id.lay_pdv);

        mLayUsuario = view.findViewById(R.id.lay_usuario);
        mLaySenha = view.findViewById(R.id.lay_senha);

        mBtNext.setOnClickListener(onNextLogin);
        mBtStep.setOnClickListener(onNextPdvEmpresa);
        mBtVoltar.setOnClickListener(onVoltar);

        mEtSenha.setOnKeyListener(onKeyPassword);

        mProgressDialog.dismiss();
        UtilKeyboard.hideKeyboard(view);
        return view;
    }

    //Validar a senha
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }

    View.OnKeyListener onKeyPassword = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (isPasswordValid(mEtSenha.getText())) {
                mTvErro.setVisibility(View.INVISIBLE);
            }
            return false;
        }
    };


    //Listem do Step do pdv
    View.OnClickListener onNextPdvEmpresa = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            UtilKeyboard.hideKeyboard(view);
            PdvEmpresaRequest pdvEmpresa = new PdvEmpresaRequest();
            mProgressDialog.show();
            mLogin = mEtLogin.getText().toString().trim();
            mSenha = mEtSenha.getText().toString().trim();
            mSenha = Utils.getHashMd5(mSenha);
            pdvEmpresa.step(mLogin, mSenha, httpListenerPdvEmpresa);
        }
    };

    //Listener do botao do login
    View.OnClickListener onNextLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UtilKeyboard.hideKeyboard(view);
            AuthRequest authRequest = new AuthRequest();
            mProgressDialog.show();
            mLogin = mEtLogin.getText().toString().trim();
            mSenha = mEtSenha.getText().toString().trim();
            mSenha = Utils.getHashMd5(mSenha);
            mPdvSelecionado = (PdvEmpresa) mSpPdvEmpresa.getSelectedItem();
            if (mPdvSelecionado.getCodigo() != null) {
                authRequest.login(mLogin, mSenha, mPdvSelecionado.getCodigo(), httpListenerLogin);
            } else {
                mDialogCustom.show();
                mDialogCustom.setTitle(mTitleDialog);
                mDialogCustom.setMessage("Selecione um pdv");
                mProgressDialog.dismiss();
            }
        }
    };

    //Listener do botao do Voltar
    View.OnClickListener onVoltar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mBtStep.setVisibility(View.VISIBLE);
            mBtVoltar.setVisibility(View.GONE);
            mBtNext.setVisibility(View.GONE);

            mLayPdv.setVisibility(View.GONE);
            mLaySenha.setVisibility(View.VISIBLE);
            mLayUsuario.setVisibility(View.VISIBLE);

            mEtLogin.setEnabled(true);
            mEtSenha.setEnabled(true);
        }
    };


    //Listener do Spinner
    AdapterView.OnItemSelectedListener onSpPdvSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i != 0) {
                mPdvSelecionado = (PdvEmpresa) adapterView.getItemAtPosition(i);
                mBtNext.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


    //Retorno da requisicao do pdvempresa
    HttpRequestListener httpListenerPdvEmpresa = new HttpRequestListener<List<PdvEmpresa>>() {
        @Override
        public void onSuccess(List<PdvEmpresa> response) {

            mBtStep.setVisibility(View.GONE);
            mBtVoltar.setVisibility(View.VISIBLE);
            mBtNext.setVisibility(View.VISIBLE);
            mLayPdv.setVisibility(View.VISIBLE);
            mLaySenha.setVisibility(View.GONE);
            mLayUsuario.setVisibility(View.GONE);
            mEtLogin.setEnabled(false);
            mEtSenha.setEnabled(false);

            PdvEmpresa pdvEmpresaSelecione = new PdvEmpresa();
            pdvEmpresaSelecione.setNome("Selecione");
            response.add(0, pdvEmpresaSelecione);
            ArrayAdapter arrayAdapter = new ArrayAdapter<PdvEmpresa>(getContext(), R.layout.spinner_layout, response);
            mSpPdvEmpresa.setAdapter(arrayAdapter);
            mLayPdv.setVisibility(View.VISIBLE);
            mSpPdvEmpresa.setOnItemSelectedListener(onSpPdvSelection);
            mProgressDialog.dismiss();
        }

        @Override
        public void onError(int code, String msg) {

            mDialogCustom.show();
            mDialogCustom.setTitle(mTitleDialog);
            mDialogCustom.setMessage(msg);
            mProgressDialog.dismiss();
        }
    };


    //Retorno do login
    HttpRequestListener httpListenerLogin = new HttpRequestListener<AuthRequest>() {
        @Override
        public void onSuccess(AuthRequest response) {

            Auth auth = new Auth(response.getLogin(), mSenha, response.getDominio(), response.getAccessToken(), mPdvSelecionado.getCodigo(), mPdvSelecionado.getNome());

            auth.setExpiresIn(response.getExpiresIn());
            auth.setUsuarioUuid(response.getUsuarioUuid());
            AppDatabase db = IngressosApplication.getAppDatabase();
            AuthRepository authRepository = new AuthRepository(db);
            authRepository.insert(auth);

            CaixaRepository caixaRepository = new CaixaRepository(db);
            Caixa mEmAberto = caixaRepository.obterAberto(auth.getUsuarioUuid());

            if (mEmAberto == null) {
//                Caixa caixa = new Caixa();
//                caixa.setAbertoEm(new Date());
//                caixa.setUuid(UUID.randomUUID().toString());
//                caixa.setAuthId(auth.getUsuarioUuid());
                caixaRepository.abrirCaixa(auth);
            }

            mTvErro.setVisibility(View.INVISIBLE);

            IngressosApplication.setAppAuth(auth);

            ((NavigationHost) getActivity()).navigateTo(new EventoFragment(), false); // Navigate to the next Fragment
            mProgressDialog.dismiss();
        }

        @Override
        public void onError(int code, String msg) {

            mDialogCustom.show();
            mDialogCustom.setMessage(msg);
            mDialogCustom.setTitle(mTitleDialog);
            mProgressDialog.dismiss();
        }
    };

}
