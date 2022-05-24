using System;
using System.Threading.Tasks;

namespace laboratorium_11
{
    class NewtonSymbol
    {
        public int K { get; set; }
        public int N { get; set; }


        public NewtonSymbol(int n, int k)
        {
            N = n;
            K = k;
        }

        public double CalculateTasks()
        {
            //sprawdzenie czy parametry ważne
            if (N <= 0 || K <= 0) return -1;
            if (N < K) return -2;

            //obliczanie licznika ułamka
            Task<double> upperTask = Task.Factory.StartNew(
                (obj) => CalculateUpper(),
                100
                );

            //obliczane mianownika
            Task<double> lowerTask = Task.Factory.StartNew(
                (obj) => CalculateLower(),
                100
                );

            //czekamy ba wyknanie zadań
            upperTask.Wait();
            lowerTask.Wait();
            return upperTask.Result / lowerTask.Result;
        }

        public double CalculateDelegates()
        {
            //obsługa błędnych parametrów
            if (N <= 0 || K <= 0) return -1;
            if (N < K) return -2;

            Func<double> counterFunc = CalculateUpper;
            Func<double> denominatorFunc = CalculateLower;
            
            //asynchroniczne wywołanie delegatów
            var counter = counterFunc.BeginInvoke(null, null);
            var denominator = denominatorFunc.BeginInvoke(null, null);

            //czekamy na zakończenie obu funkji
            while (!counter.IsCompleted && !denominator.IsCompleted) { }


            //zwracamy wynik
            return counterFunc.EndInvoke(counter) / denominatorFunc.EndInvoke(denominator);
        }



        //AsycnAwait
        public async Task<double> CalculateAsyncAwait()
        {
            //inicjalizacja zadań
            var upper = Task.Run(CalculateUpper);
            var lower =Task.Run(CalculateLower);

            //czekamy na wykonanie zadań
            await Task.WhenAll(upper, upper);

            return upper.Result /lower.Result;
        }



        private double CalculateUpper()
        {
            return Factorial(N - K + 1, N);
        }
        private double CalculateLower()
        {
            return Factorial(1, K);
        }
        private double Factorial(int from, int to)
        {
            if(from > to)
            {
                return 0;
            }
            var result = 1;
            if(to == 0 || to == 1)
            {
                return result;
            }
            else
            {
                for(int i = from; i<=to; i++)
                {
                    result *= i;
                }
                return result;
            }
        }

    }
}
