package com.digitalingressos.ingressos.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.digitalingressos.ingressos.R;

import com.digitalingressos.ingressos.ui.activity.SettingActivity;
import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.digitalingressos.ingressos.data.database.evento.Evento;
import com.digitalingressos.ingressos.data.database.evento.EventoRepository;
import com.digitalingressos.ingressos.data.database.evento.EventoRequest;

import com.digitalingressos.ingressos.ui.adpater.EventoAdapter;
import com.digitalingressos.ingressos.util.CustomDialog;


import java.util.List;
import java.util.stream.Collectors;

public class EventoFragment extends Fragment {
    RecyclerView recyclerView;
    EventoAdapter eventoAdapter;
    ProgressDialog pDialog;
    private CustomDialog mDialogCustom;
    private String mTitleDialog;

    List<Evento> eventosAtivos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void initDialogs() {

        pDialog = new ProgressDialog(this.getContext());
        pDialog.setMessage("Carregando...");
        mTitleDialog = "Atenção";
        mDialogCustom = new CustomDialog(this.getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment with the ProductGrid theme
        View view = inflater.inflate(R.layout.fragment_evento, container, false);


        requireActivity().getOnBackPressedDispatcher().addCallback(callback);
        // Set up the tool bar
        setUpToolbar(view);

        initDialogs();
        pDialog.show();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false));


        SearchView searchView = view.findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Lógica para executar a busca quando o usuário pressionar o botão de busca
                // Aqui você pode chamar o método que realiza a busca de acordo com o texto inserido
                // na caixa de pesquisa (query)

                // Exemplo:
                //realizarBusca(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                // Lógica para realizar ações enquanto o texto é digitado, antes de pressionar o botão de busca
                // Por exemplo, você pode exibir sugestões de pesquisa ou atualizar os resultados de acordo com
                // o texto digitado.
                if (eventosAtivos != null) {
                    List<Evento> eventosAtivosFilter = eventosAtivos.stream()
                            .filter(evento -> evento.getNome().toLowerCase().contains(newText.toLowerCase()))
                            .collect(Collectors.toList());

                    eventoAdapter.setFilteredList(eventosAtivosFilter);
                }
                return false;
            }
        });


        ImageView icSetting = view.findViewById(R.id.ic_setting);
        icSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        EventoRequest request = new EventoRequest();
        request.getRequest(httpListenerLogin);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Coloque aqui o código a ser executado quando o fragmento for retomado
    }

    HttpRequestListener httpListenerLogin = new HttpRequestListener<List<Evento>>() {
        @Override
        public void onSuccess(List<Evento> response) {

            EventoRepository repository = new EventoRepository(IngressosApplication.getAppDatabase());
            repository.insertAll(response);

            eventosAtivos = response.stream()
                    .filter(evento -> evento.mExibirPdvFisico && evento.getStatus().equals("ATIVO"))
                    .collect(Collectors.toList());

            eventoAdapter = new EventoAdapter(eventosAtivos);
            recyclerView.setAdapter(eventoAdapter);
            pDialog.dismiss();
        }

        @Override
        public void onError(int code, String msg) {

            pDialog.dismiss();
            mDialogCustom.show();
            mDialogCustom.setMessage(msg);
            mDialogCustom.setTitle(mTitleDialog);

        }
    };

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        //menuInflater.inflate(R.menu.shr_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {

        }
    };

}