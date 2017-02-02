package za.co.riggaroo.electricitymonitor;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.riggaroo.electricitymonitor.databinding.ActivityOverviewBinding;

public class OverviewActivity extends AppCompatActivity implements OverviewContract.View {
    private ActivityOverviewBinding binding;
    private OverviewContract.Presenter presenter;
    private ElectricityViewModel electricityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout();
        setupPresenter();
    }

    private void setupPresenter() {
        presenter = new OverviewPresenter(electricityViewModel);
        presenter.attach(this);
        presenter.loadPowerInformation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.setElectricityModel(electricityViewModel);
        binding.notifyPropertyChanged(za.co.riggaroo.electricitymonitor.BR.electricityModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    private void setupLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_overview);
        electricityViewModel = new ElectricityViewModel(getApplicationContext(), 0L, 0L, false);
        binding.setElectricityModel(electricityViewModel);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void showLoading() {
        electricityViewModel.setLoading(true);
        binding.setElectricityModel(electricityViewModel);
        binding.notifyPropertyChanged(za.co.riggaroo.electricitymonitor.BR.electricityModel);
    }

    @Override
    public void hideLoading() {
        electricityViewModel.setLoading(false);
        binding.setElectricityModel(electricityViewModel);
        binding.notifyPropertyChanged(za.co.riggaroo.electricitymonitor.BR.electricityModel);
    }

    @Override
    public void updateViewModel(final ElectricityViewModel electricityViewModel) {
        binding.setElectricityModel(electricityViewModel);
        binding.notifyPropertyChanged(za.co.riggaroo.electricitymonitor.BR.electricityModel);
        if (electricityViewModel.isPowerOn()) {
            getWindow().setStatusBarColor(
                    ContextCompat.getColor(getApplicationContext(), R.color.color_lights_on_background_darker));
        } else {
            getWindow().setStatusBarColor(
                    ContextCompat.getColor(getApplicationContext(), R.color.color_lights_off_background_darker));

        }
    }
}
