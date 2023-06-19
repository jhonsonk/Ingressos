package com.digitalingressos.ingressos.ui.adpater;

import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.util.CPFormatter;
import com.digitalingressos.ingressos.util.PhoneFormatterWatcher;
import com.digitalingressos.ingressos.viewModel.CarrinhoViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class IngressoPosseAdapter extends RecyclerView.Adapter<IngressoPosseAdapter.IngressoPosseHolder> {
    private CarrinhoViewModel mCarrinho;
    public LinearLayout mLayCart;

    private ProgressDialog loadingDialog;

    public IngressoPosseAdapter(CarrinhoViewModel carrinho, LinearLayout _linearLayoutCart) {
        mCarrinho = carrinho;
        mLayCart = _linearLayoutCart;

    }

    @Override
    public IngressoPosseHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        loadingDialog = new ProgressDialog(parent.getContext());
        loadingDialog.setMessage("Carregando");
        loadingDialog.setCancelable(false);

        return new IngressoPosseHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_ingresso_posse, parent, false), mLayCart);
    }

    @Override
    public void onBindViewHolder(IngressoPosseHolder holder, int position) {

        holder.mEtCpf.setText(mCarrinho.get(position).getCpfPosseMascara());
        holder.mEtWhatsapp.setText(mCarrinho.get(position).getTelefonePosseMascara());

        holder.mTvIngressoNome.setText(mCarrinho.get(position).getIngresso().getNome());
        holder.mTvIngressoSexo.setText(mCarrinho.get(position).getIngresso().getSexo());

        String _tipo = mCarrinho.get(position).getIngresso().getTipo();
        if (_tipo.equals("Em_Aberto")) {
            holder.mtvIngressoTipo.setText("");
            holder.mtvIngressoTipo.setVisibility(View.GONE);
        } else {
            holder.mtvIngressoTipo.setVisibility(View.VISIBLE);
            holder.mtvIngressoTipo.setText(_tipo);
        }

        holder.mTvValorTotal.setText(mCarrinho.getPrecoTotalString());


        holder.mEtCpf.addTextChangedListener(new CPFormatter(holder.mEtCpf));
        holder.mEtWhatsapp.addTextChangedListener(new PhoneFormatterWatcher(holder.mEtWhatsapp));

        if (mCarrinho.get(position).isCpfError()) {
            holder.mEtCpf.setError("Cpf Inválido");
        }

        if (mCarrinho.get(position).isTelefoneError()) {
            holder.mEtWhatsapp.setError("Telefone Inválido");
        }

        holder.mEtCpf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mCarrinho.get(position).setCpfPosse(holder.mEtCpf.getText().toString());

                if (mCarrinho.get(position).isCpfError()) {
                    holder.mEtCpf.setError("Cpf Inválido");
                }
            }
        });

        holder.mEtWhatsapp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mCarrinho.get(position).setTelefonePosse(holder.mEtWhatsapp.getText().toString());
                if (mCarrinho.get(position).isTelefoneError()) {
                    holder.mEtWhatsapp.setError("Telefone Inválido");
                }
            }
        });


        holder.mCkAPlicarTodos.setOnCheckedChangeListener(null); // Remova o listener antes de atualizar o estado do CheckBox

        holder.mCkAPlicarTodos.setChecked(mCarrinho.get(position).isAplicarTodos());

        holder.mCkAPlicarTodos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private boolean isProcessing = false;

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isProcessing) {
                    isProcessing = true;
                    loadingDialog.show();
                    if (isChecked) {
                        mCarrinho.setCheckBoxCheckedTodos(true);
                        mCarrinho.get(position).setAplicarTodos(true);
                        String cpf = mCarrinho.get(position).getCpfPosseMascara();
                        String telefonePosse = mCarrinho.get(position).getTelefonePosseMascara();

                        for (int i = 0; i < mCarrinho.getSizeCarrinho(); i++) {
                            if (i != position) {
                                mCarrinho.get(i).setCpfPosse(cpf);
                                mCarrinho.get(i).setTelefonePosse(telefonePosse);
                                mCarrinho.get(i).setAplicarTodos(false);
                                notifyItemChanged(i);
                            }
                        }
                    } else {
                        mCarrinho.setCheckBoxCheckedTodos(false);
                        mCarrinho.get(position).setAplicarTodos(true);
                        for (int i = 0; i < mCarrinho.getSizeCarrinho(); i++) {
                            if (i != position) {
                                mCarrinho.get(i).setAplicarTodos(false);
                                notifyItemChanged(i);
                            }
                        }
                    }
                    loadingDialog.dismiss();
                    isProcessing = false;
                }
            }
        });

        if (mCarrinho.isCheckBoxCheckedTodos()) {

            if (!mCarrinho.get(position).isAplicarTodos()) {
                holder.mEtCpf.setEnabled(false);
                holder.mEtWhatsapp.setEnabled(false);
                holder.mCkAPlicarTodos.setEnabled(false);
            } else {
                holder.mEtCpf.setEnabled(true);
                holder.mEtWhatsapp.setEnabled(true);
                holder.mCkAPlicarTodos.setEnabled(true);
            }
        }

        holder.mCkComprador.setOnCheckedChangeListener(null); // Remova o listener antes de atualizar o estado do CheckBox
        holder.mCkComprador.setChecked(mCarrinho.get(position).isComprador());
        holder.mCkComprador.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private boolean isProcessing = false;

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isProcessing) {
                    isProcessing = true;
                    if (isChecked) {
                        mCarrinho.get(position).setComprador(true);
                        for (int i = 0; i < mCarrinho.getSizeCarrinho(); i++) {
                            if (i != position) {
                                mCarrinho.get(i).setComprador(false);
                                notifyItemChanged(i);
                            }
                        }
                    } else {
                        mCarrinho.get(position).setComprador(false);
                    }
                    isProcessing = false;
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return mCarrinho != null ? mCarrinho.getSizeCarrinho() : 0;
    }


    public class IngressoPosseHolder extends RecyclerView.ViewHolder {

        public TextView mTvIngressoNome;
        public TextView mTvIngressoSexo;
        public TextView mtvIngressoTipo;
        public TextInputEditText mEtCpf;
        public TextInputEditText mEtWhatsapp;
        public TextView mTvValorTotal;
        public CheckBox mCkAPlicarTodos;
        public CheckBox mCkComprador;

        public IngressoPosseHolder(View itemView, View viewCarrinho) {
            super(itemView);
            mTvIngressoNome = itemView.findViewById(R.id.tv_ingresso_nome);
            mTvIngressoSexo = itemView.findViewById(R.id.tv_ingresso_sexo);
            mtvIngressoTipo = itemView.findViewById(R.id.tv_ingresso_tipo);
            mCkAPlicarTodos = itemView.findViewById(R.id.cb_aplicarTodos);
            mCkComprador = itemView.findViewById(R.id.cb_comprador);

            mEtCpf = itemView.findViewById(R.id.et_cpf);
            mEtWhatsapp = itemView.findViewById(R.id.et_whatsapp);
            mTvValorTotal = viewCarrinho.findViewById(R.id.valor_total_carrinho);
        }
    }
}


