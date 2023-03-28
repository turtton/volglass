/** @type {import("tailwindcss").Config} */
module.exports = {
	mode: "jit",
	content: ["./src/**/*.{js,ts,jsx,tsx}", "./kotlin/**/*.{js,ts,jsx,tsx}"],
	theme: {
		listStyleType: {
			nonde: "none",
			disc: "disc",
			decimal: "decimal",
			unset: "unset",
		},
		extend: {
			colors: {
				"dark-primary-alt": "#171818",
				"dark-background-primary": "#212323",
				"text-accent": "rgba(201, 19, 133, 0.77)",
				"text-accent-hover": "#1ea2cc",
				"dark-text-accent": "#892f99",
				"dark-text-accent-hover": "#25ccff",
			},
		},
	},
	plugins: [],
	darkMode: "class",
};
