import { Label } from 'recharts';

export function renderTopLeftLabel({ content, ...props }) {
  return (
    <g transform="translate(-5 -20)">
      <Label {...props} width={500}/>
    </g>
  )
}
