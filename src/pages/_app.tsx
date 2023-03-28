import "reactflow/dist/style.css";
import "../styles/global.css";
import "../styles/style.css";
import "../styles/prism.css";
import "../styles/prism-dark.css";
import { AppProps } from "next/app";
import { MathJaxContext } from "better-react-mathjax";

export default function App({ Component, pageProps }: AppProps): JSX.Element {
	return (
		<MathJaxContext>
			<Component {...pageProps} />
		</MathJaxContext>
	);
}
