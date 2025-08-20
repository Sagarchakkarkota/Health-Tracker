import { useEffect, useState } from 'react';
import {
  NativeEventEmitter,
  NativeModules,
  PermissionsAndroid,
  Platform,
  StyleSheet,
  Text,
  View,
} from 'react-native';

const { StepCounter } = NativeModules;

function App() {
  const [count, setCount] = useState(0);
  const [error, setError] = useState('');
  const requestActivityPermission = async () => {
    if (Platform.OS && +Platform.Version >= 29) {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACTIVITY_RECOGNITION,
        {
          title: '',
          message: '',
          buttonNeutral: '',
          buttonNegative: 'cancel',
          buttonPositive: 'ok',
        },
      );
      granted === PermissionsAndroid.RESULTS.GRANTED;
    }
    return true;
  };
  useEffect(() => {
    let subscription: any;

    async function initStepCounter() {
      const hasPermission = await requestActivityPermission();
      if (!hasPermission) {
        setError('Permission denied for Activity Recognition');
        return;
      }

      const stepCounterEmitter = new NativeEventEmitter(StepCounter);
      subscription = stepCounterEmitter.addListener(
        'stepCountUpdate',
        event => {
          console.log(event);
          setCount(event.stepCount);
        },
      );

      try {
        StepCounter.startSensor();
      } catch (e) {
        setError('Step counter not available on this device');
      }
    }

    initStepCounter();

    return () => {
      StepCounter.stopSensor?.();
      subscription?.remove();
    };
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>🏃 My Health Tracker</Text>
      <View style={styles.card}>
        <Text style={styles.label}>Steps Walked</Text>
        <Text style={styles.value}>{count}</Text>
      </View>
      <View style={styles.row}>
        <View style={styles.smallCard}>
          <Text style={styles.label}>Calories</Text>
          <Text style={styles.value}>{(count * 0.04).toFixed(1)}</Text>
        </View>
        <View style={styles.smallCard}>
          <Text style={styles.label}>Distance (km)</Text>
          <Text style={styles.value}>{(count * 0.0008).toFixed(2)}</Text>
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f7fa',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: '700',
    marginBottom: 30,
  },
  card: {
    backgroundColor: '#fff',
    padding: 30,
    borderRadius: 20,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOpacity: 0.1,
    shadowRadius: 10,
    elevation: 5,
    width: '90%',
    marginBottom: 20,
  },
  row: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: '90%',
  },
  smallCard: {
    backgroundColor: '#fff',
    padding: 20,
    borderRadius: 15,
    alignItems: 'center',
    flex: 1,
    marginHorizontal: 5,
    elevation: 3,
  },
  label: {
    fontSize: 16,
    color: '#555',
    marginBottom: 5,
  },
  value: {
    fontSize: 22,
    fontWeight: 'bold',
    color: '#111',
  },
});

export default App;
